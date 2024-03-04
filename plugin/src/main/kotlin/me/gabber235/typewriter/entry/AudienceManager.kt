package me.gabber235.typewriter.entry

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import lirand.api.extensions.server.server
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.interaction.AVERAGE_SCHEDULING_DELAY_MS
import me.gabber235.typewriter.interaction.TICK_MS
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.java.KoinJavaComponent.get
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

class AudienceManager : Listener {
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

    fun register() {
        unregister()

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

    fun unregister() {
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
        unregister()
        HandlerList.unregisterAll(this)
    }
}

fun Player.inAudience(entry: AudienceEntry): Boolean = inAudience(entry.ref())

fun Player.inAudience(ref: Ref<out AudienceEntry>): Boolean {
    val manager = get<AudienceManager>(AudienceManager::class.java)
    return manager[ref]?.let { return it.contains(this) } ?: false
}

val Ref<out AudienceEntry>.isActive: Boolean
    get() {
        val manager = get<AudienceManager>(AudienceManager::class.java)
        return manager[this]?.isActive ?: false
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