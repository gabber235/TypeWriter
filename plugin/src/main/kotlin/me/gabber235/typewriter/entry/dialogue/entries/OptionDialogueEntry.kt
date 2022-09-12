package me.gabber235.typewriter.entry.dialogue.entries

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.dialogue.DialogueEntry

data class OptionDialogueEntry(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("triggered_by")
	override val triggerdBy: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val triggers: List<String> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val text: String = "",
	val speaker: String = "",
	val options: List<Option> = emptyList(),
) : DialogueEntry {
	val speakerDisplayName: String
		get() = EntryDatabase.getSpeaker(speaker)?.displayName ?: ""
}

data class Option(
	val text: String = "",
	val criteria: List<Criteria> = emptyList(),
	val modifiers: List<Modifier> = emptyList(),
	val triggers: List<String> = emptyList()
)