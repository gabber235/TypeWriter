package me.gabber235.typewriter.entries.entity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.SoundSourceEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.utils.Sound
import java.util.*

@Entry("self_speaker", "The player themself", Colors.ORANGE, "bi:person-fill")
/**
 * The `Self Speaker` is a speaker that represents the player themselves.
 * This speaker is used to display messages from the player's perspective.
 *
 * ## How could this be used?
 * This speaker could be used to display messages from the player's perspective,
 * such as thoughts or internal dialogue.
 * It can also be used as a sound source for the player's voice.
 */
class SelfSpeaker(
    override val id: String = "",
    override val name: String = "",
    override val sound: Sound = Sound.EMPTY,
    @Help("Overrides the display name of the speaker")
    val overrideName: Optional<String> = Optional.empty(),
) : SpeakerEntry, SoundSourceEntry {
    override val displayName: String
        get() = overrideName.orElseGet { "%player_name%" }

    override fun getEmitter(): net.kyori.adventure.sound.Sound.Emitter {
        return net.kyori.adventure.sound.Sound.Emitter.self()
    }
}