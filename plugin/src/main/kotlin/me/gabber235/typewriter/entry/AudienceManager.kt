package me.gabber235.typewriter.entry

import lirand.api.extensions.server.server
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceFilterEntry
import me.gabber235.typewriter.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class AudienceManager : Listener {
    private var displays = emptyList<AudienceDisplay>()

    fun initialize() {
        server.pluginManager.registerEvents(this, plugin)
    }

    fun register() {
        unregister()

        val entries = Query.find<AudienceEntry>()
        val dependents = entries.filterIsInstance<AudienceFilterEntry>().flatMap { it.children }.map { it.id }.toSet()
        val roots = entries.filter { it.id !in dependents }

        displays = roots.map { it.display() }
        displays.forEach { it.initialize() }
        displays.forEach { server.onlinePlayers.forEach { player -> it.addPlayer(player) } }
    }

    fun unregister() {
        val displays = displays
        this.displays = emptyList()
        displays.forEach { it.dispose() }
    }

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        displays.forEach { it.addPlayer(event.player) }
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        displays.forEach { it.removePlayer(event.player) }
    }

    fun shutdown() {
        unregister()
        HandlerList.unregisterAll(this)
    }
}