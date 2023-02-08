package me.gabber235.typewriter.entries.action

import lirand.api.extensions.server.commands.dispatchCommand
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("player_run_command", "Make player run command", Colors.RED, Icons.TERMINAL)
data class PlayerCommandActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    private val command: String = "",

) : ActionEntry {

    /**
     * %player% will be replaced with the player's name
     * If the command starts with a "/" it will be removed
     */
    override fun execute(player: Player) {
        super.execute(player)
        player.dispatchCommand(command.replace("%player%", player.name).removePrefix("/"))
    }




}