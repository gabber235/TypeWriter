package me.gabber235.typewriter.entries.dialogue.messengers.random

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.RandomMessageDialogueEntry
import me.gabber235.typewriter.entry.dialogue.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.sendMini
import org.bukkit.entity.Player

@Messenger(RandomMessageDialogueEntry::class)
class RandomMessageDialogueDialogueMessenger(player: Player, entry: RandomMessageDialogueEntry) :
	DialogueMessenger<RandomMessageDialogueEntry>(player, entry) {

	companion object : MessengerFilter {
		override fun filter(player: Player, entry: DialogueEntry): Boolean = true
	}

	override fun tick(cycle: Int) {
		super.tick(cycle)
		if (cycle == 0) {
			val message = entry.messages.randomOrNull()?.parsePlaceholders(player) ?: return
			player.sendMini(
				"\n<gray> [ <bold>${entry.speakerDisplayName}</bold><reset><gray> ]\n<reset><white> ${message}\n"
			)
			state = MessengerState.FINISHED
		}
	}
}