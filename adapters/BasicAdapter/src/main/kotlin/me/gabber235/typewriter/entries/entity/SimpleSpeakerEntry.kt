package me.gabber235.typewriter.entries.entity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.utils.Icons

@Entry("simple_speaker", "The most basic speaker", Colors.ORANGE, Icons.PERSON)
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
    override val sound: String = "",
) : SpeakerEntry