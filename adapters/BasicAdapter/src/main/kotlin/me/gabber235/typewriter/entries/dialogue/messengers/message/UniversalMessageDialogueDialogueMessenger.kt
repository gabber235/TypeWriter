package me.gabber235.typewriter.entries.dialogue.messengers.message

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.MessageDialogueEntry
import me.gabber235.typewriter.entry.dialogue.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.*
import org.bukkit.entity.Player

@Messenger(MessageDialogueEntry::class)
class UniversalMessageDialogueDialogueMessenger(player: Player, entry: MessageDialogueEntry) :
	DialogueMessenger<MessageDialogueEntry>(player, entry) {

	companion object : MessengerFilter {
		override fun filter(player: Player, entry: DialogueEntry): Boolean = true
	}

	override fun tick(cycle: Int) {
		super.tick(cycle)
		if (cycle == 0) {
			player.sendMini(
				"\n<gray> [ <bold>${entry.speakerDisplayName}</bold><reset><gray> ]\n<reset><white> ${
					entry.text.parsePlaceholders(
						player
					).replace("\n", "\n ")
				}\n"
			)
			state = MessengerState.FINISHED
		}
	}
}