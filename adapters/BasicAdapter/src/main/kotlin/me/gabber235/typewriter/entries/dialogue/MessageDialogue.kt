package me.gabber235.typewriter.entries.dialogue

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.utils.Icons

@Entry("message", "Display a single message to the player", "#1c4da3", Icons.COMMENT)
data class MessageDialogueEntry(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("triggered_by")
	override val triggeredBy: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<String> = emptyList(),
	override val text: String = "",
	override val speaker: String = "",
) : DialogueEntry