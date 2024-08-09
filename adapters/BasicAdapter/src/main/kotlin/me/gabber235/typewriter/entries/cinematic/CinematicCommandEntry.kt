package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.*
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.ThreadType.SYNC
import org.bukkit.entity.Player

interface CinematicCommandEntry : CinematicEntry {
    val segments: List<CommandSegment>
}

@Entry(
    "cinematic_console_command",
    "Runs command as the console at a specific frame.",
    Colors.YELLOW,
    "mingcute:terminal-fill"
)
/**
 * The `Cinematic Console Command` entry runs a command as the console at a specific frame.
 *
 * ## How could this be used?
 *
 * You can use a different plugin to animate blocks, hide a scoreboard, or trigger something in another plugin.
 */
class CinematicConsoleCommandEntry(
    override val id: String,
    override val name: String,
    override val criteria: List<Criteria>,
    @Segments(Colors.YELLOW, "mingcute:terminal-fill")
    @InnerMax(Max(1))
    // Run commands on different segments
    override val segments: List<CommandSegment>,
) : CinematicCommandEntry {
    override fun create(player: Player): CinematicAction {
        return CommandAction(
            player,
            this
        ) { command ->
            player.server.dispatchCommand(player.server.consoleSender, command)
        }
    }
}

@Entry(
    "cinematic_player_command",
    "Runs command as the player at a specific frame.",
    Colors.YELLOW,
    "mingcute:terminal-fill"
)
/**
 * The `Cinematic Player Command` entry runs a command as the player at a specific frame.
 *
 * ## How could this be used?
 *
 * You can use a different plugin to animate blocks, hide a scoreboard, or trigger something in another plugin.
 */
class CinematicPlayerCommandEntry(
    override val id: String,
    override val name: String,
    override val criteria: List<Criteria>,
    @Segments(Colors.YELLOW, "mingcute:terminal-fill")
    @InnerMax(Max(1))
    // Run commands on different segments
    override val segments: List<CommandSegment>,
) : CinematicCommandEntry {
    override fun create(player: Player): CinematicAction {
        return CommandAction(
            player,
            this
        ) { command ->
            player.performCommand(command)
        }
    }
}

data class CommandSegment(
    override val startFrame: Int,
    override val endFrame: Int,
    @Help("The command(s) to run.")
    @Placeholder
    @MultiLine
    // Every line is a different command. Commands should not be prefixed with <code>/</code>.
    val command: String,
) : Segment

class CommandAction(
    private val player: Player,
    entry: CinematicCommandEntry,
    private val run: (String) -> Unit,
) : SimpleCinematicAction<CommandSegment>() {
    override val segments: List<CommandSegment> = entry.segments

    override suspend fun startSegment(segment: CommandSegment) {
        super.startSegment(segment)
        SYNC.switchContext {
            val commands = segment.command.parsePlaceholders(player).lines()
            commands.forEach(run)
        }
    }
}