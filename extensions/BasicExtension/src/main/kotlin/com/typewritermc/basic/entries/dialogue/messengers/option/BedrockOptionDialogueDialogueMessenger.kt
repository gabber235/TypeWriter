package com.typewritermc.basic.entries.dialogue.messengers.option

import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.basic.entries.dialogue.Option
import com.typewritermc.basic.entries.dialogue.OptionDialogueEntry
import com.typewritermc.core.extension.annotations.Messenger
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.dialogue.DialogueMessenger
import com.typewritermc.engine.paper.entry.dialogue.MessengerFilter
import com.typewritermc.engine.paper.entry.dialogue.MessengerState
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.entry.matches
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.utils.isFloodgate
import com.typewritermc.engine.paper.utils.legacy
import org.bukkit.entity.Player

@Messenger(OptionDialogueEntry::class, priority = 5)
class BedrockOptionDialogueDialogueMessenger(player: Player, entry: OptionDialogueEntry) :
    DialogueMessenger<OptionDialogueEntry>(player, entry) {

    companion object : MessengerFilter {
        override fun filter(player: Player, entry: DialogueEntry): Boolean = player.isFloodgate
    }

    private var selectedIndex = 0
    private val selected get() = usableOptions[selectedIndex]

    private var usableOptions: List<Option> = emptyList()

    override val triggers: List<Ref<out TriggerableEntry>>
        get() = entry.triggers + selected.triggers

    override val modifiers: List<Modifier>
        get() = entry.modifiers + selected.modifiers


    override fun init() {
        super.init()
        usableOptions = entry.options.filter { it.criteria.matches(player) }
        org.geysermc.floodgate.api.FloodgateApi.getInstance().sendForm(
            player.uniqueId,
            org.geysermc.cumulus.form.CustomForm.builder()
                .title("<bold>${entry.speakerDisplayName}</bold>".legacy())
                .label("${entry.text.parsePlaceholders(player).legacy()}\n\n\n")
                .dropdown(
                    "Select Response",
                    usableOptions.map { it.text.parsePlaceholders(player).legacy() })
                .label("\n\n\n\n")
                .closedOrInvalidResultHandler { _, _ ->
                    state = MessengerState.CANCELLED
                }
                .validResultHandler { responds ->
                    val dropdown = responds.asDropdown()
                    selectedIndex = dropdown
                    state = MessengerState.FINISHED
                }
        )
    }

    override fun end() {
        // Do nothing as we don't need to resend the messages.
    }
}