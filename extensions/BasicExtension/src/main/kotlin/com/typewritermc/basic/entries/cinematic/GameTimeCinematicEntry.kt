package com.typewritermc.basic.entries.cinematic

import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTimeUpdate
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.core.utils.point.squared
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.entries.CinematicAction
import com.typewritermc.engine.paper.entry.entries.PrimaryCinematicEntry
import com.typewritermc.engine.paper.entry.entries.Segment
import com.typewritermc.engine.paper.entry.temporal.SimpleCinematicAction
import com.typewritermc.engine.paper.extensions.packetevents.sendPacketTo
import com.typewritermc.engine.paper.interaction.InterceptionBundle
import com.typewritermc.engine.paper.interaction.interceptPackets
import com.typewritermc.engine.paper.utils.GenericPlayerStateProvider.GAME_TIME
import com.typewritermc.engine.paper.utils.PlayerState
import com.typewritermc.engine.paper.utils.restore
import com.typewritermc.engine.paper.utils.state
import org.bukkit.entity.Player

@Entry("game_time_cinematic", "A cinematic that changes the in game time", Colors.CYAN, "material-symbols:auto-timer")
/**
 * The `GameTimeCinematicEntry` is an entry that changes the in game time during a cinematic.
 *
 * The time on the segment is the in game time in ticks at the end of the segment.
 * It will do an easing animation to the time.
 *
 * The total time of a Minecraft day is `24000` ticks.
 * Some examples of times are:
 * ------------------------------
 * | Time of day | Ticks        |
 * |-------------|--------------|
 * | Dawn        | 0            |
 * | Noon        | 6000         |
 * | Dusk        | 12000        |
 * | Midnight    | 18000        |
 * ------------------------------
 *
 * ## How could this be used?
 * This can be used to simulate the time passing during a cinematic.
 */
class GameTimeCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(Colors.CYAN, "material-symbols:auto-timer")
    val segments: List<GameTimeSegment> = emptyList(),
) : PrimaryCinematicEntry {
    override fun create(player: Player): CinematicAction = GameTimeCinematicAction(player, this)
}

data class GameTimeSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    val time: Int = 0,
) : Segment

class GameTimeCinematicAction(
    val player: Player,
    entry: GameTimeCinematicEntry,
) : SimpleCinematicAction<GameTimeSegment>() {
    override val segments: List<GameTimeSegment> = entry.segments

    private var state: PlayerState? = null

    private var tracker: TimeTracker? = null
    private var interceptor: InterceptionBundle? = null

    override suspend fun setup() {
        super.setup()
        state = player.state(GAME_TIME)
    }

    override suspend fun startSegment(segment: GameTimeSegment) {
        super.startSegment(segment)
        tracker = TimeTracker(player.playerTime, player.playerTime - (player.playerTime % 24000) + segment.time)

        // Since minecraft sends the time update packet every 20 ticks, we can't use it and needs to send our own.
        // But it twitches when we still allow that packet to be sent. Therefore, we want to make sure that the send time is correct.
        interceptor = player.interceptPackets {
            PacketType.Play.Server.TIME_UPDATE { event ->
                val packet = WrapperPlayServerTimeUpdate(event)
                tracker?.let { packet.timeOfDay = it.lastTime }
            }
        }
    }


    override suspend fun stopSegment(segment: GameTimeSegment) {
        super.stopSegment(segment)
        // We don't reset the time for the player since it is likely we want to keep the time set to this.
        // Otherwise, why change it?
        interceptor?.cancel()
        player.setPlayerTime(segment.time.toLong(), false)
        tracker = null
    }

    override suspend fun tickSegment(segment: GameTimeSegment, frame: Int) {
        super.tickSegment(segment, frame)

        val percentage = (frame - segment.startFrame).toDouble() / (segment.endFrame - segment.startFrame)
        val easedPercentage = percentage.easeInOutQuad()
        val time = tracker?.time(easedPercentage) ?: return
        player.setPlayerTime(time, false)
        WrapperPlayServerTimeUpdate(player.world.gameTime, time).sendPacketTo(player)
    }

    override suspend fun teardown() {
        super.teardown()
        player.restore(state)
    }


}

private data class TimeTracker(
    val startTime: Long,
    val endTime: Long,
) {
    var lastTime: Long = startTime
        private set

    fun time(percent: Double): Long {
        lastTime = startTime + ((endTime - startTime) * percent).toLong()
        return lastTime
    }
}

private fun Double.easeInOutQuad(): Double {
    return if (this < 0.5) {
        2 * this * this
    } else {
        1 - (-2 * this + 2).squared() / 2
    }
}