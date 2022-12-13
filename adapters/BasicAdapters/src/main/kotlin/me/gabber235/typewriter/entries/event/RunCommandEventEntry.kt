package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.event.player.PlayerCommandPreprocessEvent

@Entry("run_command", "When a player runs a command", Colors.YELLOW, Icons.TERMINAL)
class RunCommandEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val command: String = "",
) : EventEntry

@EntryListener(RunCommandEventEntry::class)
fun onRunCommand(event: PlayerCommandPreprocessEvent, query: Query<RunCommandEventEntry>) {
	val message = event.message.removePrefix("/")

	query findWhere { Regex(it.command).matches(message) } triggerAllFor event.player
}