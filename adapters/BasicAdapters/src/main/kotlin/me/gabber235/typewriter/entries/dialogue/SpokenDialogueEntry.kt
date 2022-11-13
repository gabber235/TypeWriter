package me.gabber235.typewriter.entries.dialogue

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.DialogueEntry

@Entry("spoken", "Display a animated message to the player")
data class SpokenDialogueEntry(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("triggered_by")
	override val triggerdBy: List<String> = emptyList(),
	override val triggers: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val text: String = "",
	override val speaker: String = "",
	val duration: Int = 40, // in ticks
) : DialogueEntry