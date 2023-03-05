package me.gabber235.typewriter.entries.dialogue.messengers.message

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.MessageDialogueEntry
import me.gabber235.typewriter.entry.dialogue.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

val messageFormat: String by snippet(
	"dialogue.message.format",
	"\n<gray> [ <bold><speaker></bold><reset><gray> ]\n<reset><white> <message>\n"
)

@Messenger(MessageDialogueEntry::class)
class UniversalMessageDialogueDialogueMessenger(player: Player, entry: MessageDialogueEntry) :
	DialogueMessenger<MessageDialogueEntry>(player, entry) {

	companion object : MessengerFilter {
		override fun filter(player: Player, entry: DialogueEntry): Boolean = true
	}

	override fun tick(cycle: Int) {
		super.tick(cycle)
		if (cycle == 0) {
			player.sendMessageDialogue(entry.text, entry.speakerDisplayName.parsePlaceholders(player))
			state = MessengerState.FINISHED
		}
	}
}

fun Player.sendMessageDialogue(text: String, speakerDisplayName: String) {
	sendMiniWithResolvers(
		messageFormat,
		Placeholder.parsed("speaker", speakerDisplayName),
		Placeholder.parsed("message", text.parsePlaceholders(player).replace("\n", "\n "))
	)
}