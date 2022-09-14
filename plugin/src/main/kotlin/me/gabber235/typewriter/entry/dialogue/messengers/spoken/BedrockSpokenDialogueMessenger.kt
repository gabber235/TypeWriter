package me.gabber235.typewriter.entry.dialogue.messengers.spoken

import me.gabber235.typewriter.entry.dialogue.entries.SpokenDialogueEntry
import me.gabber235.typewriter.entry.dialogue.messengers.Messenger
import me.gabber235.typewriter.entry.dialogue.messengers.MessengerState
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.legacy
import org.bukkit.entity.Player
import org.geysermc.cumulus.form.SimpleForm
import org.geysermc.floodgate.api.FloodgateApi

class BedrockSpokenDialogueMessenger(player: Player, entry: SpokenDialogueEntry) :
	Messenger<SpokenDialogueEntry>(player, entry) {
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