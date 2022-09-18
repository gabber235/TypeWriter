package me.gabber235.typewriter.entry.dialogue

import me.gabber235.typewriter.entry.*

interface DialogueEntry : RuleEntry {
	val speaker: String
	val text: String
	val speakerDisplayName: String
		get() = speakerEntry?.displayName ?: ""

	val speakerEntry: SpeakerEntry?
		get() = EntryDatabase.getSpeaker(speaker)
}

