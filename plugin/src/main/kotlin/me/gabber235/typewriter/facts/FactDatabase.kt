package me.gabber235.typewriter.facts

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.ModifierOperator
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.ExpirableFactEntry
import me.gabber235.typewriter.entry.entries.PersistableFactEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.entry.entries.WritableFactEntry
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import me.gabber235.typewriter.utils.logErrorIfNull
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

class FactDatabase : KoinComponent {
    private val storage: FactStorage by inject()

    // Local stored version of player facts
    private val cache = ConcurrentHashMap<FactId, FactData>()

    fun initialize() {
        storage.init()

        // Load all the facts from the storage
        runBlocking {
            loadFactsFromPersistentStorage()
        }

        // Filter expired facts every second.
        // After that, save the facts of the players who have facts that expired or changed.
        DISPATCHERS_ASYNC.launch {
            var cycle = 0
            while (plugin.isEnabled) {
                delay(1000)
                removeExpiredFacts()

                if (cycle++ % 60 == 0) {
                    storeFactsInPersistentStorage()
                }
            }
        }
    }

    fun shutdown() {
        runBlocking {
            storeFactsInPersistentStorage()
        }
        storage.shutdown()
    }

    private suspend fun loadFactsFromPersistentStorage() {
        val facts = storage.loadFacts()
        cache.clear()
        cache.putAll(facts)
    }

    private suspend fun storeFactsInPersistentStorage() {
        logger.info("Storing facts in persistent storage")

        val entries =
            cache.keys.mapNotNull { Query.findById<PersistableFactEntry>(it.entryId) }.associateBy { it.id }

        val facts = cache.entries.filter { (id, data) ->
            val entry = entries[id.entryId] ?: return@filter false

            // If the fact is not persistable, or it has expired, don't store it.
            if (!entry.canPersist(id, data)) return@filter false
            if (entry is ExpirableFactEntry && entry.hasExpired(id, data)) return@filter false

            true
        }.map { (id, data) -> id to data }

        storage.storeFacts(facts)
    }

    private fun removeExpiredFacts() {
        val entries = cache.keys.mapNotNull { Query.findById<ExpirableFactEntry>(it.entryId) }.associateBy { it.id }

        cache.entries.removeIf { (id, data) ->
            val entry = entries[id.entryId] ?: return@removeIf false
            entry.hasExpired(id, data)
        }
    }

    internal operator fun get(id: FactId): FactData? = cache[id]

    internal operator fun set(id: FactId, data: FactData) {
        if (data.value == 0) {
            cache.remove(id)
        } else {
            cache[id] = data
        }
    }

    fun modify(player: Player, modifiers: List<Modifier>) {
        modify(player) {
            modifiers.forEach { modifier ->
                this[modifier.fact] = when (modifier.operator) {
                    ModifierOperator.ADD -> {
                        val entry =
                            modifier.fact.get().logErrorIfNull("Could not find ${modifier.fact}") ?: return@forEach

                        if (entry !is ReadableFactEntry) {
                            plugin.logger.warning("Tried to add to a non-readable fact: ${modifier.fact}, how do you expect to add if you can't read?")
                            return@forEach
                        }

                        val fact = entry.readForPlayer(player)
                        modifier.value + fact.value
                    }

                    ModifierOperator.SET -> modifier.value
                }
            }
        }
    }

    fun modify(player: Player, modifier: FactsModifier.() -> Unit) {
        val modifications = FactsModifier().apply(modifier).build()
        if (modifications.isEmpty()) return

        for ((id, value) in modifications) {
            val entry = Query.findById<WritableFactEntry>(id) ?: continue
            entry.write(player, value)
        }
    }
}

class FactsModifier {
    private val modifications = mutableMapOf<String, Int>()

    operator fun set(ref: Ref<out WritableFactEntry>, value: Int) = set(ref.id, value)

    operator fun set(id: String, value: Int) {
        modifications[id] = value
    }

    fun build(): Map<String, Int> = modifications
}

fun Player.fact(ref: Ref<out ReadableFactEntry>) = ref.get()?.readForPlayer(this)