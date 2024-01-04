package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.TriggerableEntry

@Tags("dialogue")
interface DialogueEntry : TriggerableEntry {
    @Help("The speaker of the dialogue")
    @EntryIdentifier(SpeakerEntry::class)
    val speaker: String

    val speakerDisplayName: String
        get() = speakerEntry?.displayName ?: ""

    val speakerEntry: SpeakerEntry?
        get() = Query.findById(speaker)
}

