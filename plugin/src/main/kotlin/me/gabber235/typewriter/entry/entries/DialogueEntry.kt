package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders

@Tags("dialogue")
interface DialogueEntry : TriggerableEntry {
	@Help("The speaker of the dialogue")
	@EntryIdentifier(SpeakerEntry::class)
	val speaker: String
	//val text: String


	val speakerDisplayName: String
		get() = speakerEntry?.displayName?.parsePlaceholders(null) ?: ""

	val speakerEntry: SpeakerEntry?
		get() = Query.findById(speaker)
}

