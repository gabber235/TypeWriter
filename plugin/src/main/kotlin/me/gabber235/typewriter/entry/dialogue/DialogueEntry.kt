package me.gabber235.typewriter.entry.dialogue

import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.RuleEntry

interface DialogueEntry : RuleEntry {
	val speaker: String
	val text: String
	val speakerDisplayName: String
		get() = EntryDatabase.getSpeaker(speaker)?.displayName ?: ""
}

