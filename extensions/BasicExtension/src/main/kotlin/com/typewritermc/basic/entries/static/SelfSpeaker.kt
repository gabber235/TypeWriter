package com.typewritermc.basic.entries.static

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.SoundEmitter
import com.typewritermc.engine.paper.entry.entries.SoundSourceEntry
import com.typewritermc.engine.paper.entry.entries.SpeakerEntry
import com.typewritermc.engine.paper.utils.Sound
import org.bukkit.entity.Player
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
    val overrideName: Optional<String> = Optional.empty(),
) : SpeakerEntry, SoundSourceEntry {
    override val displayName: String
        get() = overrideName.orElseGet { "%player_name%" }

    override fun getEmitter(player: Player): SoundEmitter = SoundEmitter(player.entityId)
}