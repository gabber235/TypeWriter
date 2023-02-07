package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.TriggerableEntry

@Tags("dialogue")
interface DialogueEntry : TriggerableEntry {
	@EntryIdentifier(SpeakerEntry::class)
	val speaker: String
	//val text: String


	val speakerDisplayName: String
		get() = speakerEntry?.displayName ?: ""

	val speakerEntry: SpeakerEntry?
		get() = EntryDatabase.getEntity<SpeakerEntry>(speaker)
} 

