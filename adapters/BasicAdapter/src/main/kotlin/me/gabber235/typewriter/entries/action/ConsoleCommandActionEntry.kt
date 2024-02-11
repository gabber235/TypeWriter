package me.gabber235.typewriter.entries.action

import lirand.api.extensions.server.commands.dispatchCommand
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.ThreadType.SYNC
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@Entry("console_run_command", "Run command from console", Colors.RED, "mingcute:terminal-fill")
/**
 * The Console Command Action is an action that sends a command to the server console. This action provides you with the ability to execute console commands on the server in response to specific events.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to perform administrative tasks, such as sending a message to all players on the server, or to automate server tasks, such as setting the time of day or weather conditions. The possibilities are endless!
 */
class ConsoleCommandActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Placeholder
    @MultiLine
    @Help("The command(s) to run.")
    // Every line is a different command. Commands should not be prefixed with <code>/</code>.
    private val command: String = "",
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)
        // Run in the main thread
        SYNC.launch {
            val commands = command.parsePlaceholders(player).lines()
            for (cmd in commands) {
                Bukkit.getConsoleSender().dispatchCommand(cmd)
            }
        }
    }
}