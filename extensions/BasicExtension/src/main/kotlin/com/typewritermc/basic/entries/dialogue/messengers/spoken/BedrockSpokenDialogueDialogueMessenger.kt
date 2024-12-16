package com.typewritermc.basic.entries.dialogue.messengers.spoken

import com.typewritermc.basic.entries.dialogue.SpokenDialogueEntry
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.engine.paper.entry.dialogue.DialogueMessenger
import com.typewritermc.engine.paper.entry.dialogue.MessengerState
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.utils.legacy
import org.bukkit.entity.Player

class BedrockSpokenDialogueDialogueMessenger(player: Player, context: InteractionContext, entry: SpokenDialogueEntry) :
    DialogueMessenger<SpokenDialogueEntry>(player, context, entry) {

    override fun init() {
        super.init()
        org.geysermc.floodgate.api.FloodgateApi.getInstance().sendForm(
            player.uniqueId,
            org.geysermc.cumulus.form.SimpleForm.builder()
                .title("<bold>${entry.speakerDisplayName.get(player).parsePlaceholders(player)}</bold>".legacy())
                .content("${entry.text.get(player).parsePlaceholders(player).legacy()}\n\n")
                .button("Continue")
                .closedOrInvalidResultHandler { _, _ ->
                    state = MessengerState.CANCELLED
                }
                .validResultHandler { _, _ ->
                    state = MessengerState.FINISHED
                }
        )
    }

    override fun end() {
        // Do nothing as we don't need to resend the messages.
    }
}