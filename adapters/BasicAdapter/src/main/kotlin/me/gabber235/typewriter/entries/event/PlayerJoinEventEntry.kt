package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.EntryListener
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.triggerAllFor
import me.gabber235.typewriter.utils.Icons
import org.bukkit.event.player.PlayerJoinEvent

@Entry("on_player_join", "When the player joins the server", Colors.YELLOW, Icons.PERSON_CIRCLE_PLUS)
data class PlayerJoinEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(PlayerJoinEventEntry::class)
fun onJoin(event: PlayerJoinEvent, query: Query<PlayerJoinEventEntry>) {
    println("Player ${event.player.name} joined the server: ${query.find()}")
    query.find() triggerAllFor event.player
}