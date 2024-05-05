package me.gabber235.typewriter.entries.dialogue.messengers.random

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.RandomMessageDialogueEntry
import me.gabber235.typewriter.entries.dialogue.messengers.message.sendMessageDialogue
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.dialogue.MessengerState
import me.gabber235.typewriter.entry.entries.DialogueEntry
import org.bukkit.entity.Player
import java.time.Duration

@Messenger(RandomMessageDialogueEntry::class)
class RandomMessageDialogueDialogueMessenger(player: Player, entry: RandomMessageDialogueEntry) :
    DialogueMessenger<RandomMessageDialogueEntry>(player, entry) {

    companion object : MessengerFilter {
        override fun filter(player: Player, entry: DialogueEntry): Boolean = true
    }

    override fun tick(playTime: Duration) {
        super.tick(playTime)
        if (state != MessengerState.RUNNING) return
        state = MessengerState.FINISHED
        val message = entry.messages.randomOrNull() ?: return
        player.sendMessageDialogue(message, entry.speakerDisplayName)
    }
}