package me.gabber235.typewriter.entries.entity

import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.utils.*
import kotlin.jvm.optionals.getOrDefault

@Entry("simple_speaker", "The most basic speaker", Colors.ORANGE, "bi:person-fill")
/**
 * The `Spoken Dialogue Action` is an action that displays an animated message to the player. This action provides you with the ability to display a message with a specified speaker, text, and duration.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to create storylines, provide instructions to players, or create immersive roleplay experiences. The possibilities are endless!
 */
class SimpleSpeakerEntry(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
) : SpeakerEntry

@EntryMigration(SimpleSpeakerEntry::class, "0.4.0")
@NeedsMigrationIfNotParsable
fun migrate040SimpleSpeaker(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "sound")

    val soundId = json.getAndParse<String>("sound", context.gson).optional

    val sound = Sound(
        soundId = soundId.map(::DefaultSoundId).getOrDefault(SoundId.EMPTY),
        soundSource = SelfSoundSource,
        volume = 1.0f,
        pitch = 1.0f,
    )

    data["sound"] = context.gson.toJsonTree(sound)

    return data
}
