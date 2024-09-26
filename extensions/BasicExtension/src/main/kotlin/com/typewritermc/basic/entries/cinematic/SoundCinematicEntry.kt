package com.typewritermc.basic.entries.cinematic

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.engine.paper.entry.cinematic.SimpleCinematicAction
import com.typewritermc.engine.paper.entry.entries.CinematicAction
import com.typewritermc.engine.paper.entry.entries.CinematicEntry
import com.typewritermc.engine.paper.entry.entries.Segment
import com.typewritermc.engine.paper.utils.*
import org.bukkit.entity.Player

@Entry("sound_cinematic", "Play a sound during a cinematic", Colors.YELLOW, "fa6-solid:music")
/**
 * The `Sound Cinematic` entry plays a sound during a cinematic.
 *
 * ## How could this be used?
 *
 * This entry could be used to play a sound during a cinematic, such as a sound effect for a cutscene.
 */
class SoundCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(icon = "fa6-solid:music", color = Colors.YELLOW)
    val segments: List<SoundSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return SoundCinematicAction(
            player,
            this,
        )
    }
}

data class SoundSegment(
    override val startFrame: Int,
    override val endFrame: Int,
    val sound: Sound,
) : Segment

class SoundCinematicAction(
    private val player: Player,
    private val entry: SoundCinematicEntry,
) : SimpleCinematicAction<SoundSegment>() {
    override val segments: List<SoundSegment> = entry.segments

    override suspend fun startSegment(segment: SoundSegment) {
        super.startSegment(segment)
        player.playSound(segment.sound)
    }

    override suspend fun stopSegment(segment: SoundSegment) {
        super.stopSegment(segment)
        player.stopSound(segment.sound)
    }
}