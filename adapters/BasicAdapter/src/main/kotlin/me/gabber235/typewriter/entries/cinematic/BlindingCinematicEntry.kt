package me.gabber235.typewriter.entries.cinematic

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.extensions.packetevents.sendPacketTo
import org.bukkit.entity.Player

@Entry("blinding_cinematic", "Blind the player so the screen looks black", Colors.CYAN, "heroicons-solid:eye-off")
/**
 * The `Blinding Cinematic` entry is used to blind the player so the screen looks black.
 *
 * ## How could this be used?
 * Make the screen look black for the player during a cinematic.
 */
class BlindingCinematicEntry(
    override val id: String,
    override val name: String,
    override val criteria: List<Criteria>,
    @Segments(icon = "heroicons-solid:eye-off")
    val segments: List<BlindingSegment>,
) : CinematicEntry {
    override fun createSimulated(player: Player): CinematicAction? = null
    override fun create(player: Player): CinematicAction {
        return BlindingCinematicAction(
            player,
            this,
        )
    }
}

data class BlindingSegment(
    override val startFrame: Int,
    override val endFrame: Int,
) : Segment

class BlindingCinematicAction(
    private val player: Player,
    private val entry: BlindingCinematicEntry,
) : SimpleCinematicAction<BlindingSegment>() {
    override val segments: List<BlindingSegment> = entry.segments

    override suspend fun tickSegment(segment: BlindingSegment, frame: Int) {
        super.tickSegment(segment, frame)

        val packet = WrapperPlayServerChangeGameState(WrapperPlayServerChangeGameState.Reason.WIN_GAME, 1f)
        packet.sendPacketTo(player)
    }

    override suspend fun stopSegment(segment: BlindingSegment) {
        super.stopSegment(segment)
        val packet = WrapperPlayServerCloseWindow(0)
        packet.sendPacketTo(player)
    }


    override suspend fun teardown() {
        super.teardown()
        val packet = WrapperPlayServerCloseWindow(0)
        packet.sendPacketTo(player)
    }
}
