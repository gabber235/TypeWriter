package me.gabber235.typewriter.interaction

import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.dialogue.DialogueSequence
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_END
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_NEXT
import me.gabber235.typewriter.entry.matches
import org.bukkit.entity.Player

class Interaction(val player: Player) {
	private var dialogue: DialogueSequence? = null
	val hasDialogue: Boolean
		get() = dialogue != null

	fun tick() {
		dialogue?.tick()
	}

	/**
	 * Handles an event.
	 * All [SystemTrigger]'s are handled by the plugin itself.
	 */
	fun onEvent(event: Event) {
		triggerActions(event)

		if (DIALOGUE_NEXT in event) {
			onDialogueNext()
			return
		}
		if (DIALOGUE_END in event) {
			dialogue?.end()
			dialogue = null
			return
		}

		// Try to trigger new/next dialogue
		tryTriggerNextDialogue(event)
	}

	/**
	 * Triggers all actions that are registered for the given event.
	 *
	 * @param event The event that should be triggered
	 */
	private fun triggerActions(event: Event) {
		// Trigger all actions
		val actions = Query.findWhere<ActionEntry> { it in event && it.criteria.matches(event.player.uniqueId) }
		actions.forEach { action ->
			action.execute(event.player)
		}
		val newTriggers = actions.flatMap { it.triggers }
			.map { EntryTrigger(it) }
			.filter { it !in event } // Stops infinite loops
		if (newTriggers.isNotEmpty()) {
			InteractionHandler.triggerEvent(Event(event.player, newTriggers))
		}
	}


	/**
	 * Tries to trigger a new dialogue.
	 * If no dialogue can be found, it will end the dialogue sequence.
	 */
	private fun tryTriggerNextDialogue(event: Event) {
		val nextDialogue = Query.findWhere<DialogueEntry> { it in event }
			.sortedByDescending { it.criteria.size }
			.firstOrNull { it.criteria.matches(event.player.uniqueId) }

		if (nextDialogue != null) {
			// If there is no sequence yet, start a new one
			if (dialogue == null) {
				dialogue = DialogueSequence(player, nextDialogue)
				dialogue?.init()
			} else {
				// If there is a sequence, trigger the next dialogue
				dialogue?.next(nextDialogue)
			}
		} else if (dialogue?.isActive == false) {
			// If there is no next dialogue and the sequence isn't active anymore, we can end the sequence
			InteractionHandler.triggerEvent(Event(player, DIALOGUE_END))
		}
	}

	/**
	 * Called when the player clicks the next button.
	 * If there is no next dialogue, the sequence will be ended.
	 */
	private fun onDialogueNext() {
		val dialog = dialogue ?: return
		if (dialog.triggers.isEmpty()) {
			InteractionHandler.triggerEvent(Event(player, DIALOGUE_END))
			return
		}
		val triggers = dialog.triggers.map { EntryTrigger(it) }
		InteractionHandler.triggerEvent(Event(player, triggers))
		return
	}

	fun end() {
		dialogue?.end()
	}
}