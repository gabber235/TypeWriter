package me.gabber235.typewriter.entries.action

import com.google.gson.JsonObject
import lirand.api.extensions.other.set
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.*
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import kotlin.jvm.optionals.getOrDefault

@Entry("play_sound", "Play sound at player, or location", Colors.RED, "fa6-solid:volume-high")
/**
 * The `Play Sound Action` is an action that plays a sound for the player. This action provides you with the ability to play any sound that is available in Minecraft, at a specified location.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to provide audio feedback to players, such as when they successfully complete a challenge, or to create ambiance in your Minecraft world, such as by playing background music or sound effects. The possibilities are endless!
 */
class PlaySoundActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The sound to play.")
    val sound: Sound = Sound.EMPTY,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        player.playSound(sound)
    }
}

@EntryMigration(PlaySoundActionEntry::class, "0.4.0")
@NeedsMigrationIfNotParsable
fun migrate040PlaySoundAction(json: JsonObject, context: EntryMigratorContext): JsonObject {
    val data = JsonObject()
    data.copyAllBut(json, "sound", "location", "volume", "pitch")

    val soundString = json.getAndParse<String>("sound", context.gson).optional
    val location = json.getAndParse<Optional<Location>>("location", context.gson).optional
    val volume = json.getAndParse<Float>("volume", context.gson).optional
    val pitch = json.getAndParse<Float>("pitch", context.gson).optional

    val sound = Sound(
        soundId = soundString.map(::DefaultSoundId).getOrDefault(SoundId.EMPTY),
        soundSource = location.map(::LocationSoundSource).getOrDefault(SelfSoundSource),
        volume = volume.orElse(1.0f),
        pitch = pitch.orElse(1.0f),
    )

    data["sound"] = context.gson.toJsonTree(sound)

    return data
}