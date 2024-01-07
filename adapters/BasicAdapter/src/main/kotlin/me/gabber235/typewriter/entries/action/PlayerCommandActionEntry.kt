package me.gabber235.typewriter.entries.action

import com.github.shynixn.mccoroutine.bukkit.launch
import lirand.api.extensions.server.commands.dispatchCommand
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("player_run_command", "Make player run command", Colors.RED, Icons.TERMINAL)
/**
 * The `Player Command Action` is an action that runs a command as if the player entered it.
 * This action provides you with the ability to execute commands on behalf of the player in response to specific events.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations.
 * You can use it to provide players with a custom command that triggers a specific action,
 * such as teleporting the player to a specific location or giving them an item.
 * You can also use it to automate repetitive tasks,
 * such as sending a message to the player when they complete a quest or achievement.
 * The possibilities are endless!
 */
class PlayerCommandActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    @Placeholder
    @MultiLine
    @Help("The command(s) to run.")
    // Every line is a different command. Commands should not be prefixed with <code>/</code>.
    private val command: String = "",
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)
        // Run in main thread
        plugin.launch {
            val commands = command.parsePlaceholders(player).lines()
            for (cmd in commands) {
                player.dispatchCommand(cmd)
            }
        }
    }
}