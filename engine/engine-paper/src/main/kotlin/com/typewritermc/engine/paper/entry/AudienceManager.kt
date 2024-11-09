package com.typewritermc.engine.paper.entry

import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.ref
import com.typewritermc.core.utils.Reloadable
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import lirand.api.extensions.server.server
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.interaction.AVERAGE_SCHEDULING_DELAY_MS
import com.typewritermc.engine.paper.interaction.TICK_MS
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.ThreadType.DISPATCHERS_ASYNC
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.java.KoinJavaComponent.get
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

class AudienceManager : Listener, Reloadable {
    private var displays = emptyMap<Ref<out AudienceEntry>, AudienceDisplay>()
    private var parents = emptyMap<Ref<out AudienceEntry>, List<Ref<out AudienceFilterEntry>>>()
    private var roots = emptyList<Ref<out AudienceEntry>>()
    private var job: Job? = null

    fun initialize() {
        server.pluginManager.registerEvents(this, plugin)
        job = DISPATCHERS_ASYNC.launch {
            while (plugin.isEnabled) {
                val startTime = System.currentTimeMillis()
                displays.values.asSequence()
                    .filter { it.isActive }
                    .filterIsInstance<TickableDisplay>()
                    .forEach {
                        try {
                            it.tick()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                val endTime = System.currentTimeMillis()
                // Wait for the remainder or the tick
                val wait = TICK_MS - (endTime - startTime) - AVERAGE_SCHEDULING_DELAY_MS
                if (wait > 0) delay(wait)
            }
        }
    }

    override fun load() {
        unload()

        val entries = Query.find<AudienceEntry>()

        val parents = mutableMapOf<Ref<out AudienceEntry>, List<Ref<out AudienceFilterEntry>>>()
        entries.filterIsInstance<AudienceFilterEntry>().forEach { entry ->
            entry.children.forEach { child ->
                val list = parents.getOrPut(child) { mutableListOf() }
                (list as MutableList).add(entry.ref())
            }
        }
        this.parents = parents

        roots = entries
            .filter { parents[it.ref()].isNullOrEmpty() }
            .filter {
                !it::class.hasAnnotation<ChildOnly>()
            }
            .map { it.ref() }
            .toList()

        displays = entries.associate { it.ref() to it.display() }

        server.onlinePlayers.forEach { player ->
            addPlayerForRoots(player)
        }
    }

    fun addPlayerFor(player: Player, ref: Ref<out AudienceEntry>) {
        val display = displays[ref] ?: return
        display.addPlayer(player)
    }

    fun addPlayerForRoots(player: Player) {
        roots.forEach { addPlayerFor(player, it) }
    }

    fun removePlayerFor(player: Player, ref: Ref<out AudienceEntry>) {
        val display = displays[ref] ?: return
        display.removePlayer(player)
    }

    fun removePlayerForRoots(player: Player) {
        roots.forEach { removePlayerFor(player, it) }
    }

    fun addPlayerToChildren(player: Player, ref: Ref<out AudienceFilterEntry>) {
        val entry = ref.get() ?: return
        entry.children.forEach { addPlayerFor(player, it) }
    }

    fun removePlayerFromChildren(player: Player, ref: Ref<out AudienceFilterEntry>) {
        val entry = ref.get() ?: return
        entry.children
            .filter { entryRef -> getParents(entryRef).none { this[it]?.contains(player) ?: false } }
            .forEach { removePlayerFor(player, it) }
    }

    fun <D : Any> findDisplays(klass: KClass<D>): Sequence<D> {
        return displays.values.asSequence().filterIsInstance(klass.java)
    }

    operator fun get(ref: Ref<out AudienceEntry>): AudienceDisplay? = displays[ref]

    fun getParents(ref: Ref<out AudienceEntry>): List<Ref<out AudienceFilterEntry>> = parents[ref] ?: emptyList()

    override fun unload() {
        val displays = displays
        this.displays = emptyMap()
        displays.values.forEach { it.dispose() }
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        addPlayerForRoots(event.player)
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        removePlayerForRoots(event.player)
    }

    fun shutdown() {
        job?.cancel()
        job = null
        unload()
        HandlerList.unregisterAll(this)
    }
}

fun Player.inAudience(entry: AudienceEntry): Boolean = inAudience(entry.ref())

fun Player.inAudience(ref: Ref<out AudienceEntry>): Boolean {
    val manager = get<AudienceManager>(AudienceManager::class.java)
    return manager[ref]?.let { return it.contains(this) } ?: false
}

fun Player.audienceState(entry: AudienceEntry): AudienceDisplayState = audienceState(entry.ref())
fun Player.audienceState(ref: Ref<out AudienceEntry>): AudienceDisplayState {
    val manager = get<AudienceManager>(AudienceManager::class.java)
    return manager[ref]?.displayState(this) ?: AudienceDisplayState.NOT_CONSIDERED
}

val Ref<out AudienceEntry>.isActive: Boolean
    get() {
        val manager = get<AudienceManager>(AudienceManager::class.java)
        return manager[this]?.isActive ?: false
    }

fun Ref<out AudienceEntry>.findDisplay(): AudienceDisplay? {
    val manager = get<AudienceManager>(AudienceManager::class.java)
    return manager[this]
}

fun <E : AudienceEntry> List<Ref<out AudienceEntry>>.descendants(klass: KClass<E>): List<Ref<E>> {
    return flatMap {
        val child = it.get() ?: return@flatMap emptyList<Ref<E>>()
        if (klass.isInstance(child)) {
            listOf(it as Ref<E>)
        } else if (child is AudienceFilterEntry) {
            child.children.descendants(klass)
        } else {
            emptyList()
        }
    }
}

fun <E : AudienceEntry> Ref<out AudienceFilterEntry>.descendants(klass: KClass<E>): List<Ref<E>> {
    val entry = get() ?: return emptyList()
    return entry.children.descendants(klass)
}