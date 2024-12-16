package com.typewritermc.basic.entries.dialogue.messengers.input

import com.typewritermc.basic.entries.dialogue.InputDialogueEntry
import com.typewritermc.core.interaction.EntryContextKey
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.engine.paper.entry.dialogue.DialogueMessenger
import com.typewritermc.engine.paper.entry.dialogue.MessengerState
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.utils.legacy
import org.bukkit.entity.Player
import org.geysermc.cumulus.form.CustomForm

class BedrockInputDialogueDialogueMessenger<T : Any>(
    player: Player,
    context: InteractionContext,
    entry: InputDialogueEntry,
    private val key: EntryContextKey,
    private val parser: (String) -> Result<T>,
) : DialogueMessenger<InputDialogueEntry>(player, context, entry) {

    override fun init() {
        super.init()
        sendForm()
    }

    private fun sendForm() {
        org.geysermc.floodgate.api.FloodgateApi.getInstance().sendForm(
            player.uniqueId,
            CustomForm.builder()
                .title("<bold>${entry.speakerDisplayName.get(player).parsePlaceholders(player)}</bold>".legacy())
                .label("${entry.text.get(player).parsePlaceholders(player).legacy()}\n\n")
                .input("Value")
                .closedOrInvalidResultHandler { _, _ ->
                    state = MessengerState.CANCELLED
                }
                .validResultHandler { _, response ->
                    val input = response.asInput()
                    if (input == null) {
                        sendForm()
                        return@validResultHandler
                    }
                    val value = parser(input)
                    if (value.isFailure) {
                        sendForm()
                        return@validResultHandler
                    }
                    val data = value.getOrNull() ?: return@validResultHandler
                    context[entry, key] = data
                    state = MessengerState.FINISHED
                }
        )
    }
}