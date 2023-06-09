package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.entry.entries.canFinishAt
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.msg
import org.bukkit.entity.Player

@Entry("empty_cinematic", "Test entry to test out how the cinematic system works", Colors.CYAN, Icons.TENGE_SIGN)
data class EmptyCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments
    val segments: List<EmptySegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return EmptyCinematicAction(
            player,
            this,
        )
    }
}

data class EmptySegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
) : Segment

class EmptyCinematicAction(
    private val player: Player,
    private val entry: EmptyCinematicEntry,
) : CinematicAction {

    override suspend fun tick(frame: Int) {
        super.tick(frame)

        player.msg("Tick $frame")
        println("Tick $frame")
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}