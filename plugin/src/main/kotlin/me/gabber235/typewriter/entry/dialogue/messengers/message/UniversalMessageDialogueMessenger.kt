package me.gabber235.typewriter.entry.dialogue.messengers.message

import me.gabber235.typewriter.entry.dialogue.entries.MessageDialogueEntry
import me.gabber235.typewriter.entry.dialogue.messengers.Messenger
import me.gabber235.typewriter.entry.dialogue.messengers.MessengerState
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.sendMini
import org.bukkit.entity.Player

class UniversalMessageDialogueMessenger(player: Player, entry: MessageDialogueEntry) :
	Messenger<MessageDialogueEntry>(player, entry) {
	override fun tick(cycle: Int) {
		super.tick(cycle)
		if (cycle == 0) {
			player.sendMini(
				"\n<gray> [ <bold>${entry.speakerDisplayName}</bold> ]\n<reset><white> ${
					entry.text.parsePlaceholders(
						player
					)
				}\n"
			)
			state = MessengerState.FINISHED
		}
	}
}