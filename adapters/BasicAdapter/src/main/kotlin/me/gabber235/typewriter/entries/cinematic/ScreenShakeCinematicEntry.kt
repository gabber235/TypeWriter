package me.gabber235.typewriter.entries.cinematic

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHurtAnimation
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.packetevents.sendPacketTo
import org.bukkit.entity.Player
import kotlin.random.Random

@Entry(
    "screen_shake_cinematic",
    "Shake the screen",
    Colors.CYAN,
    "ant-design:shake-outlined"
)
/**
 * The `Screen Shake Cinematic` entry is used to shake the screen.
 *
 * ## How could this be used?
 * It could be used to simulate an earthquake or a sudden impact.
 */
class ScreenShakeCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(icon = "ant-design:shake-outlined")
    val segments: List<ScreenShakeSegment>
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return ScreenShakeCinematicAction(
            player,
            this
        )
    }
}

data class ScreenShakeSegment(
    override val startFrame: Int,
    override val endFrame: Int,
    @Help("The amount of frames to wait before the next shake.")
    val frameDelay: Int,
) : Segment

class ScreenShakeCinematicAction(
    private val player: Player,
    private val entry: ScreenShakeCinematicEntry,
) : CinematicAction {

    override suspend fun tick(frame: Int) {
        super.tick(frame)

        val segment = (entry.segments activeSegmentAt frame) ?: return
        val baseFrame = frame - segment.startFrame
        if (segment.frameDelay > 0 && baseFrame % segment.frameDelay != 0) return

        val packet = WrapperPlayServerHurtAnimation(player.entityId, Random.nextFloat() * 360 - 180)
        packet.sendPacketTo(player)
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}