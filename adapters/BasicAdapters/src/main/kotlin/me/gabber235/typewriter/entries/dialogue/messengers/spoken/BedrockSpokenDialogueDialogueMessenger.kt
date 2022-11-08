package me.gabber235.typewriter.entries.dialogue.messengers.spoken

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.SpokenDialogueEntry
import me.gabber235.typewriter.entry.dialogue.*
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.isFloodgate
import me.gabber235.typewriter.utils.legacy
import org.bukkit.entity.Player
import org.geysermc.cumulus.form.SimpleForm
import org.geysermc.floodgate.api.FloodgateApi

@Messenger(SpokenDialogueEntry::class, priority = 5)
class BedrockSpokenDialogueDialogueMessenger(player: Player, entry: SpokenDialogueEntry) :
	DialogueMessenger<SpokenDialogueEntry>(player, entry) {

	companion object : MessengerFilter {
		override fun filter(player: Player, entry: DialogueEntry): Boolean = player.isFloodgate
	}

	override fun init() {
		super.init()
		FloodgateApi.getInstance().sendForm(
			player.uniqueId,
			SimpleForm.builder()
				.title("<bold>${entry.speakerDisplayName}</bold>".legacy())
				.content("${entry.text.parsePlaceholders(player).legacy()}\n\n")
				.button("Continue")
				.closedOrInvalidResultHandler { _, _ ->
					state = MessengerState.CANCELLED
				}
				.validResultHandler { _, _ ->
					state = MessengerState.FINISHED
				}
		)
	}
}