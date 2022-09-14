package me.gabber235.typewriter.interaction

import me.gabber235.typewriter.entry.EntryDatabase
import me.gabber235.typewriter.entry.dialogue.DialogueSequence
import me.gabber235.typewriter.entry.event.Event
import me.gabber235.typewriter.facts.facts
import org.bukkit.entity.Player

class Interaction(val player: Player) {
	private var dialogue: DialogueSequence? = null

	val isActive get() = dialogue != null

	fun tick() {
		dialogue?.tick()
	}

	fun onEvent(event: Event) {
		if (event.id == "system.dialogue.next") {
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
		if (event.id == "system.dialogue.end") {
			dialogue?.end()
			dialogue = null
			return
		}

		// Try to trigger new/next dialogue
		val facts = player.facts
		val nextDialogue = EntryDatabase.findDialogue(event.id, facts)
		if (nextDialogue != null) {
			if (dialogue == null) {
				dialogue = DialogueSequence(player, nextDialogue)
				dialogue?.init()
			} else {
				dialogue?.next(nextDialogue)
			}
		} else if (dialogue?.isActive == false) {
			InteractionHandler.triggerEvent(Event("system.dialogue.end", player))
		}

	}

	fun end() {
		dialogue?.end()
	}
}