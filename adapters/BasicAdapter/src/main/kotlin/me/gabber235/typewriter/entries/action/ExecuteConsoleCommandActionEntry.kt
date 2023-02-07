package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("execute_console_command", "Execute a command as the console", Colors.RED, Icons.TERMINAL)
class ExecuteConsoleCommandActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria>,
	override val modifiers: List<Modifier>,
	override val triggers: List<String> = emptyList(),
	private val command: String = "",
) : ActionEntry {
	override fun execute(player: Player) {
		val command = this.command.parsePlaceholders(player)
		player.server.dispatchCommand(player.server.consoleSender, command)
		super.execute(player)
	}
}