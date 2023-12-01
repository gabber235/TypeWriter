package me.gabber235.typewriter.entries.cinematic

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.utils.*
import org.bukkit.entity.Player
import kotlin.jvm.optionals.getOrDefault
import net.kyori.adventure.sound.Sound as AdventureSound

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

@EntryMigration(SoundCinematicEntry::class, "0.4.0")
@NeedsMigrationIfNotParsable
fun migrate040SoundCinematic(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "segments", "channel")

    val channel = json.getAndParse<AdventureSound.Source>("channel", context.gson).optional

    val segmentsJson = json["segments"]
    if (segmentsJson?.isJsonArray == true) {
        logger.severe("Tried migrating sound cinematic entry ${json["name"]}, but segments were not an array.")
        return data
    }

    val segmentsArray = segmentsJson?.asJsonArray ?: JsonArray()

    val segments = segmentsArray.mapNotNull {
        if (!it.isJsonObject) {
            logger.severe("Tried migrating sound cinematic entry ${json["name"]}, but segment was not an object.")
            return@mapNotNull null
        }

        val segmentJson = it.asJsonObject
        val startFrame = segmentJson.getAndParse<Int>("startFrame", context.gson).optional
        val endFrame = segmentJson.getAndParse<Int>("endFrame", context.gson).optional
        val sound = segmentJson.getAndParse<String>("sound", context.gson).optional
        val volume = segmentJson.getAndParse<Float>("volume", context.gson).optional
        val pitch = segmentJson.getAndParse<Float>("pitch", context.gson).optional

        SoundSegment(
            startFrame = startFrame.getOrDefault(0),
            endFrame = endFrame.getOrDefault(0),
            sound = Sound(
                soundId = sound.map(::DefaultSoundId).getOrDefault(SoundId.EMPTY),
                soundSource = SelfSoundSource,
                track = channel.getOrDefault(AdventureSound.Source.MASTER),
                volume = volume.getOrDefault(1.0f),
                pitch = pitch.getOrDefault(1.0f),
            )
        )
    }

    data["segments"] = context.gson.toJsonTree(segments)


    return data
}