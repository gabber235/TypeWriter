package me.gabber235.typewriter.entries.dialogue

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.utils.Icons

@Entry("option", "Display a list of options to the player", "#4CAF50", Icons.LIST_UL)
data class OptionDialogueEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<String> = emptyList(),
	override val speaker: String = "",
	val text: String = "",
	val options: List<Option> = emptyList(),
) : DialogueEntry

data class Option(
	val text: String = "",
	val criteria: List<Criteria> = emptyList(),
	val modifiers: List<Modifier> = emptyList(),
	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	val triggers: List<String> = emptyList()
)