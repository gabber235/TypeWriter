package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.SoundId
import net.kyori.adventure.sound.SoundStop
import org.bukkit.entity.Player

@Entry("sound_cinematic", "Play a sound during a cinematic", Colors.YELLOW, Icons.MUSIC)
/**
 * The `Sound Cinematic` entry plays a sound during a cinematic.
 *
 * ## How could this be used?
 *
 * This entry could be used to play a sound during a cinematic, such as a sound effect for a cutscene.
 */
class SoundCinematicEntry(
    override val id: String,
    override val name: String,
    override val criteria: List<Criteria>,
    @Segments(icon = Icons.MUSIC)
    val segments: List<SoundSegment>,
    @Help("The channel to play the sound in")
    val channel: net.kyori.adventure.sound.Sound.Source = net.kyori.adventure.sound.Sound.Source.MASTER,
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
    val sound: SoundId = SoundId.EMPTY,
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

    override suspend fun startSegment(segment: SoundSegment) {
        super.startSegment(segment)
        player.playSound(
            net.kyori.adventure.sound.Sound.sound(
                segment.sound.namespacedKey ?: return,
                entry.channel,
                segment.volume,
                segment.pitch,
            ),
        )
    }

    override suspend fun stopSegment(segment: SoundSegment) {
        super.stopSegment(segment)
        player.stopSound(SoundStop.named(segment.sound.namespacedKey ?: return))
    }

}