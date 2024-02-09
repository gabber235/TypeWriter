package me.gabber235.typewriter.entries.dialogue.messengers.option

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.Option
import me.gabber235.typewriter.entries.dialogue.OptionDialogueEntry
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.dialogue.MessengerState
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.matches
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.isFloodgate
import me.gabber235.typewriter.utils.legacy
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