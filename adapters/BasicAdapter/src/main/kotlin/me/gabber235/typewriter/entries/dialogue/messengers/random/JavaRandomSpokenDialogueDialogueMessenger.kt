package me.gabber235.typewriter.entries.dialogue.messengers.random

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.RandomSpokenDialogueEntry
import me.gabber235.typewriter.entries.dialogue.messengers.spoken.sendSpokenDialogue
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.dialogue.MessengerState
import me.gabber235.typewriter.entry.entries.DialogueEntry
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerSwapHandItemsEvent

@Messenger(RandomSpokenDialogueEntry::class)
class JavaRandomSpokenDialogueDialogueMessenger(player: Player, entry: RandomSpokenDialogueEntry) :
    DialogueMessenger<RandomSpokenDialogueEntry>(player, entry) {

    companion object : MessengerFilter {
        override fun filter(player: Player, entry: DialogueEntry): Boolean = true
    }

    private var speakerDisplayName = ""
    private var text = ""

    override fun init() {
        super.init()
        speakerDisplayName = entry.speakerDisplayName
        text = entry.messages.randomOrNull() ?: ""

        listen<PlayerSwapHandItemsEvent> { event ->
            if (event.player.uniqueId != player.uniqueId) return@listen
            state = MessengerState.FINISHED
            event.isCancelled = true
        }
    }

    override fun tick(cycle: Int) {
        if (state != MessengerState.RUNNING) return
        player.sendSpokenDialogue(text, speakerDisplayName, entry.duration, cycle, triggers.isEmpty())
    }
}