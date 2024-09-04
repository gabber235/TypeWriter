package com.typewritermc.basic.entries.dialogue.messengers.random

import com.typewritermc.basic.entries.dialogue.RandomSpokenDialogueEntry
import com.typewritermc.basic.entries.dialogue.messengers.spoken.sendSpokenDialogue
import com.typewritermc.core.extension.annotations.Messenger
import com.typewritermc.engine.paper.entry.dialogue.DialogueMessenger
import com.typewritermc.engine.paper.entry.dialogue.MessengerFilter
import com.typewritermc.engine.paper.entry.dialogue.MessengerState
import com.typewritermc.engine.paper.entry.dialogue.confirmationKey
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import org.bukkit.entity.Player
import java.time.Duration

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

        confirmationKey.listen(this, player.uniqueId) {
            state = MessengerState.FINISHED
        }
    }

    override fun tick(playTime: Duration) {
        if (state != MessengerState.RUNNING) return
        player.sendSpokenDialogue(
            text.parsePlaceholders(player),
            speakerDisplayName.parsePlaceholders(player),
            entry.duration,
            playTime,
            triggers.isEmpty()
        )
    }
}