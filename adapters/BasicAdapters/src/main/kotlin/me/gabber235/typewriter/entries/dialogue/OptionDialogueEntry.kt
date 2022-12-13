package me.gabber235.typewriter.entries.dialogue

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.SnakeCase
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.utils.Icons

@Entry("option", "Display a list of options to the player", "#4CAF50", Icons.LIST_UL)
data class OptionDialogueEntry(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("triggered_by")
	override val triggeredBy: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val triggers: List<String> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val text: String = "",
	override val speaker: String = "",
	val options: List<Option> = emptyList(),
) : DialogueEntry

data class Option(
	val text: String = "",
	val criteria: List<Criteria> = emptyList(),
	val modifiers: List<Modifier> = emptyList(),
	@field:Triggers
	@field:SnakeCase
	val triggers: List<String> = emptyList()
)