package me.gabber235.typewriter.entry

import lirand.api.extensions.server.server
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceFilterEntry
import me.gabber235.typewriter.plugin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.java.KoinJavaComponent.get
import kotlin.reflect.KClass

class AudienceManager : Listener {
    private var displays = emptyMap<Ref<out AudienceEntry>, AudienceDisplay>()
    private var roots = emptyList<Ref<out AudienceEntry>>()

    fun initialize() {
        server.pluginManager.registerEvents(this, plugin)
    }

    fun register() {
        unregister()

        val entries = Query.find<AudienceEntry>()
        val dependents = entries.filterIsInstance<AudienceFilterEntry>().flatMap { it.children }.map { it.id }.toSet()
        roots = entries.filter { it.id !in dependents }.map { it.ref() }

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
        entry.children.forEach { removePlayerFor(player, it) }
    }

    operator fun get(ref: Ref<out AudienceEntry>): AudienceDisplay? = displays[ref]

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
        unregister()
        HandlerList.unregisterAll(this)
    }

}

fun Player.inAudience(ref: Ref<out AudienceEntry>): Boolean {
    val manager = get<AudienceManager>(AudienceManager::class.java)
    return manager[ref]?.let { return it.contains(this) } ?: false
}

fun <E : AudienceEntry> Ref<out AudienceFilterEntry>.descendants(klass: KClass<E>): List<Ref<E>> {
    val entry = get() ?: return emptyList()
    return entry.children.flatMap {
        val child = it.get() ?: return@flatMap emptyList<Ref<E>>()
        if (klass.isInstance(child)) {
            listOf(it as Ref<E>)
        } else if (child is AudienceFilterEntry) {
            child.ref().descendants(klass)
        } else {
            emptyList()
        }
    }
}