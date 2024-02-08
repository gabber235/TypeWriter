package me.gabber235.typewriter.facts

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.ModifierOperator
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.ExpirableFactEntry
import me.gabber235.typewriter.entry.entries.PersistableFactEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.entry.entries.WritableFactEntry
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import me.gabber235.typewriter.utils.logErrorIfNull
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

class FactDatabase : KoinComponent {
    private val storage: FactStorage by inject()

    // Local stored version of player facts
    private val cache = ConcurrentHashMap<UUID, Set<Fact>>()

    // Queue of players which facts need to be saved
    private val flush = ConcurrentLinkedDeque<UUID>()

    fun initialize() {
        storage.init()

        // Load facts for players before they join.
        // This way we can delay the loading screen.
        plugin.listen<AsyncPlayerPreLoginEvent> { event ->
            runBlocking {
                loadFactsFromPersistentStorage(event.uniqueId)
            }
        }

        // Save facts when a player leaves
        plugin.listen<PlayerQuitEvent> { event ->
            val facts = cache.remove(event.player.uniqueId)
            if (facts != null) {
                DISPATCHERS_ASYNC.launch {
                    storeFactsInPersistentStorage(event.player.uniqueId)
                }
            }
        }

        // Filter expired facts every second.
        // After that, save the facts of the players who have facts that expired or changed.
        DISPATCHERS_ASYNC.launch {
            while (plugin.isEnabled) {
                delay(1000)
                cache.keys.forEach { uuid ->
                    removeExpiredFacts(uuid)
                }

                while (flush.isNotEmpty()) {
                    storeFactsInPersistentStorage(flush.poll())
                }
            }
        }
    }

    fun shutdown() {
        runBlocking {
            cache.keys.forEach { playerId ->
                storeFactsInPersistentStorage(playerId)
            }
        }
        flush.clear()
        storage.shutdown()
    }

    private suspend fun loadFactsFromPersistentStorage(playerId: UUID) {
        val facts = storage.loadFacts(playerId)
        cache[playerId] = facts
    }

    private suspend fun storeFactsInPersistentStorage(playerId: UUID) {
        val facts =
            cache[playerId]?.filter {
                val entry = Query.findById<PersistableFactEntry>(it.id) ?: return@filter false

                // If the fact is not persistable, or it has expired, don't store it.
                if (!entry.canPersist(it)) return@filter false
                if (entry is ExpirableFactEntry && entry.hasExpired(it)) return@filter false

                true
            }?.toSet()
                ?: return
        storage.storeFacts(playerId, facts)
    }

    private fun removeExpiredFacts(playerId: UUID) {
        cache.computeIfPresent(playerId) { _, facts ->
            val newFacts = facts.filter {
                val entry = Query.findById<ExpirableFactEntry>(it.id) ?: return@filter true
                !entry.hasExpired(it)
            }.toSet()

            if (newFacts.size < facts.size) {
                val needsFlush = playerId !in flush && facts.filter { it !in newFacts }.any { fact ->
                    Query.findById<PersistableFactEntry>(fact.id) != null
                }
                if (needsFlush) flush.add(playerId)
            }

            newFacts
        }
    }

    internal fun getCachedFact(playerId: UUID, id: String): Fact? {
        return cache[playerId]?.find { it.id == id }
    }

    internal fun setCachedFact(playerId: UUID, fact: Fact) {
        cache.compute(playerId) { _, facts ->
            val newFacts = facts?.filter { it.id != fact.id }?.toSet() ?: emptySet()
            if (fact.value == 0) return@compute newFacts
            newFacts + fact
        }
        if (playerId in flush) return
        if (Query.findById<PersistableFactEntry>(fact.id) != null) flush.add(playerId)
    }

    fun modify(playerId: UUID, modifiers: List<Modifier>) {
        modify(playerId) {
            modifiers.forEach { modifier ->
                this[modifier.fact] = when (modifier.operator) {
                    ModifierOperator.ADD -> {
                        val entry =
                            modifier.fact.get().logErrorIfNull("Could not find ${modifier.fact}") ?: return@forEach

                        if (entry !is ReadableFactEntry) {
                            plugin.logger.warning("Tried to add to a non-readable fact: ${modifier.fact}, how do you expect to add if you can't read?")
                            return@forEach
                        }

                        val fact = entry.read(playerId)
                        modifier.value + fact.value
                    }

                    ModifierOperator.SET -> modifier.value
                }
            }
        }
    }

    fun modify(playerId: UUID, modifier: FactsModifier.() -> Unit) {
        val modifications = FactsModifier().apply(modifier).build()
        if (modifications.isEmpty()) return

        val hasPersistentFact = modifications.map { (id, value) ->
            val entry = Query.findById<WritableFactEntry>(id) ?: return@map false
            entry.write(playerId, value)
            entry is PersistableFactEntry
        }.any()

        if (hasPersistentFact) flush.add(playerId)
    }

    internal fun listCachedFacts(uniqueId: UUID): Set<Fact> = cache[uniqueId] ?: emptySet()
}

class FactsModifier {
    private val modifications = mutableMapOf<String, Int>()

    operator fun set(ref: Ref<out WritableFactEntry>, value: Int) = set(ref.id, value)

    operator fun set(id: String, value: Int) {
        modifications[id] = value
    }

    fun build(): Map<String, Int> = modifications
}

fun Player.fact(ref: Ref<out ReadableFactEntry>) = ref.get()?.read(uniqueId)