package me.gabber235.typewriter.entries.entities

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.utils.Icons

@Entry("simple_speaker", "The most basic speaker", Colors.ORANGE, Icons.PERSON)
data class SimpleSpeakerEntry(
	override val id: String = "",
	override val name: String = "",
	override val displayName: String = "",
	override val sound: String = "",
) : SpeakerEntry