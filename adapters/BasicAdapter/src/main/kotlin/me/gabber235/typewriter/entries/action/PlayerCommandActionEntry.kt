package me.gabber235.typewriter.entries.action

import com.github.shynixn.mccoroutine.bukkit.launch
import lirand.api.extensions.server.commands.dispatchCommand
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("player_run_command", "Make player run command", Colors.RED, Icons.TERMINAL)
data class PlayerCommandActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    @Help("The command to run. (Use %player_name% for the player's name)")
    private val command: String = "",
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)
        // Run in main thread
        plugin.launch {
            player.dispatchCommand(command.parsePlaceholders(player))
        }
    }
}