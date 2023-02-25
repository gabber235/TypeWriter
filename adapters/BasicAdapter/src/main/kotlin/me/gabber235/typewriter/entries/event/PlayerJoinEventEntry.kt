package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.event.player.PlayerJoinEvent
import java.util.*

@Entry("on_player_join", "When the player joins the server", Colors.YELLOW, Icons.PERSON_CIRCLE_PLUS)
class PlayerJoinEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(PlayerJoinEventEntry::class)
fun onJoin(event: PlayerJoinEvent, query: Query<PlayerJoinEventEntry>) {
	query.find() triggerAllFor event.player
}