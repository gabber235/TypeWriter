package com.typewritermc.basic.entries.cinematic

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.entries.CinematicAction
import com.typewritermc.engine.paper.entry.entries.EmptyCinematicAction
import com.typewritermc.engine.paper.entry.entries.PrimaryCinematicEntry
import com.typewritermc.engine.paper.entry.entries.Segment
import com.typewritermc.engine.paper.entry.temporal.SimpleCinematicAction
import com.typewritermc.engine.paper.extensions.packetevents.sendPacketTo
import com.typewritermc.engine.paper.utils.isFloodgate
import org.bukkit.entity.Player

@Entry("blinding_cinematic", "Blind the player so the screen looks black", Colors.CYAN, "heroicons-solid:eye-off")
/**
 * The `Blinding Cinematic` entry is used to blind the player so the screen looks black.
 *
 * ## How could this be used?
 * Make the screen look black for the player during a cinematic.
 */
class BlindingCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(icon = "heroicons-solid:eye-off")
    val segments: List<BlindingSegment> = emptyList(),
) : PrimaryCinematicEntry {
    override fun createSimulating(player: Player): CinematicAction? = null
    override fun create(player: Player): CinematicAction {
        // Disable for bedrock players as it doesn't give the desired effect
        if (player.isFloodgate) return EmptyCinematicAction
        return BlindingCinematicAction(
            player,
            this,
        )
    }
}

data class BlindingSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
) : Segment

class BlindingCinematicAction(
    private val player: Player,
    entry: BlindingCinematicEntry,
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
