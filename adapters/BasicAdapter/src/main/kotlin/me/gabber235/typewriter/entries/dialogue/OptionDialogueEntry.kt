package me.gabber235.typewriter.entries.dialogue

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.*
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
	@Help("The text to display to the player.")
	val text: String = "",
	@Help("The options for the player to choose from.")
	val options: List<Option> = emptyList(),
) : DialogueEntry

data class Option(
	@Help("Text for this option.")
	val text: String = "",
	@Help("The criteria that must be met for this option to show.")
	val criteria: List<Criteria> = emptyList(),
	@Help("The modifiers to apply when this option is chosen.")
	val modifiers: List<Modifier> = emptyList(),
	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	@Help("The triggers to fire when this option is chosen.")
	val triggers: List<String> = emptyList()
)