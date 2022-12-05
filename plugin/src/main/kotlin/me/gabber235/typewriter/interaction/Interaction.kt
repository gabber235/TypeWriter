package me.gabber235.typewriter.interaction

import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.dialogue.DialogueSequence
import me.gabber235.typewriter.entry.entries.Event
import me.gabber235.typewriter.facts.facts
import org.bukkit.entity.Player

class Interaction(val player: Player) {
	private var dialogue: DialogueSequence? = null

	val isActive get() = dialogue != null

	fun tick() {
		dialogue?.tick()
	}

	/**
	 * Handles an event.
	 * All events that start with "system." are handled by the plugin itself.
	 */
	fun onEvent(event: Event) {
		if (event.id == "system.dialogue.next") {
			onDialogueNext()
			return
		}
		if (event.id == "system.dialogue.end") {
			dialogue?.end()
			dialogue = null
			return
		}

		// Try to trigger new/next dialogue
		tryTriggerNextDialogue(event)
	}

	/**
	 * Tries to trigger a new dialogue.
	 * If no dialogue can be found, it will end the dialogue sequence.
	 */
	private fun tryTriggerNextDialogue(event: Event) {
		val facts = player.facts
		val nextDialogue = EntryDatabase.findDialogue(event.id, facts)
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
			InteractionHandler.triggerEvent(Event("system.dialogue.end", player))
		}
	}

	/**
	 * Called when the player clicks the next button.
	 * If there is no next dialogue, the sequence will be ended.
	 */
	private fun onDialogueNext() {
		val dialog = dialogue ?: return
		if (dialog.triggers.isEmpty()) {
			InteractionHandler.triggerEvent(Event("system.dialogue.end", player))
			return
		}
		dialog.triggers.forEach {
			InteractionHandler.triggerEvent(Event(it, player))
		}
		return
	}

	fun end() {
		dialogue?.end()
	}
}