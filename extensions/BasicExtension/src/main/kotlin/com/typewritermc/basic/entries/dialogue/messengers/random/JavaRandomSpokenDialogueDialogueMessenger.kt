package com.typewritermc.basic.entries.dialogue.messengers.random

import com.typewritermc.basic.entries.dialogue.RandomSpokenDialogueEntry
import com.typewritermc.basic.entries.dialogue.messengers.spoken.sendSpokenDialogue
import com.typewritermc.core.extension.annotations.Messenger
import com.typewritermc.engine.paper.entry.dialogue.*
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.utils.stripped
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
    private var typingDuration = Duration.ZERO
    private var playedTime = Duration.ZERO

    override fun init() {
        super.init()
        speakerDisplayName = entry.speakerDisplayName
        text = entry.messages.randomOrNull() ?: ""
        typingDuration = typingDurationType.totalDuration(text.stripped(), entry.duration)

        confirmationKey.listen(this, player.uniqueId) {
            completeOrFinish()
        }
    }

    override fun tick(context: TickContext) {
        if (state != MessengerState.RUNNING) return
        playedTime += context.deltaTime
        player.sendSpokenDialogue(
            text.parsePlaceholders(player),
            speakerDisplayName.parsePlaceholders(player),
            entry.duration,
            playedTime,
            triggers.isEmpty()
        )
    }
}