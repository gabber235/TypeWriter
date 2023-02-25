package me.gabber235.typewriter.entries.dialogue

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.utils.Icons
import java.time.Duration

@Entry("random_spoken", "Display a random selected animated message to the player", "#1E88E5", Icons.MESSAGE)
data class RandomSpokenDialogueEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<String> = emptyList(),
	override val speaker: String = "",
	@Help("The text to display to the player. One will be picked at random.")
	val messages: List<String> = emptyList(),
	@Help("The duration it takes to type out the message.")
	val duration: Duration = Duration.ZERO,
) : DialogueEntry