package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.StaticEntryIdentifier
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.RuleEntry

@Tags("dialogue")
interface DialogueEntry : RuleEntry {
	@StaticEntryIdentifier(SpeakerEntry::class)
	val speaker: String
	val text: String

	val speakerDisplayName: String
		get() = speakerEntry?.displayName ?: ""

	val speakerEntry: SpeakerEntry?
		get() = EntryDatabase.getEntity<SpeakerEntry>(speaker)
} 

