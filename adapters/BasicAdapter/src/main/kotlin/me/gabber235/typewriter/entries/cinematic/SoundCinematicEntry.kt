package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.adapters.modifiers.Sound
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("sound_cinematic", "Play a sound during a cinematic", Colors.YELLOW, Icons.MUSIC)
class SoundCinematicEntry(
    override val id: String,
    override val name: String,
    override val criteria: List<Criteria>,
    @Segments(icon = Icons.MUSIC)
    val segments: List<SoundSegment>,
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
    @Help("The sound to play")
    @Sound
    val sound: String = "",
    @Help("The volume of the sound")
    val volume: Float = 1f,
    @Help("The pitch of the sound")
    val pitch: Float = 1f,
) : Segment

class SoundCinematicAction(
    private val player: Player,
    private val entry: SoundCinematicEntry,
) : SimpleCinematicAction<SoundSegment>() {

    override val segments: List<SoundSegment> = entry.segments

    override fun startSegment(segment: SoundSegment) {
        super.startSegment(segment)
        player.playSound(player.location, segment.sound, segment.volume, segment.pitch)
    }

    override fun stopSegment(segment: SoundSegment) {
        super.stopSegment(segment)
        player.stopSound(segment.sound)
    }

}