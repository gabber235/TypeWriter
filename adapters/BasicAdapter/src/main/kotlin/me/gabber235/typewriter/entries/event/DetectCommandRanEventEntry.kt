package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.event.player.PlayerCommandPreprocessEvent

@Entry("on_detect_command_ran", "When a player runs an existing command", Colors.YELLOW, Icons.TERMINAL)
class DetectCommandRanEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	private val command: String = "",
) : EventEntry

@EntryListener(DetectCommandRanEventEntry::class)
fun onRunCommand(event: PlayerCommandPreprocessEvent, query: Query<RunCommandEventEntry>) {
	val message = event.message.removePrefix("/")

	query findWhere { Regex(it.command).matches(message) } triggerAllFor event.player
}