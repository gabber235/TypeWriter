package com.typewritermc.basic.entries.dialogue.messengers.message

import com.typewritermc.basic.entries.dialogue.MessageDialogueEntry
import com.typewritermc.core.extension.annotations.Messenger
import com.typewritermc.engine.paper.entry.dialogue.DialogueMessenger
import com.typewritermc.engine.paper.entry.dialogue.MessengerFilter
import com.typewritermc.engine.paper.entry.dialogue.MessengerState
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.snippets.snippet
import com.typewritermc.engine.paper.utils.sendMiniWithResolvers
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import java.time.Duration

val messageFormat: String by snippet(
    "dialogue.message.format",
    "\n<gray> [ <bold><speaker></bold><reset><gray> ]\n<reset><white> <message>\n"
)

val messagePadding: String by snippet(
    "dialogue.message.padding",
    " "
)

@Messenger(MessageDialogueEntry::class)
class UniversalMessageDialogueDialogueMessenger(player: Player, entry: MessageDialogueEntry) :
    DialogueMessenger<MessageDialogueEntry>(player, entry) {

    companion object : MessengerFilter {
        override fun filter(player: Player, entry: DialogueEntry): Boolean = true
    }

    override fun tick(playTime: Duration) {
        super.tick(playTime)
        if (state != MessengerState.RUNNING) return
        state = MessengerState.FINISHED
        player.sendMessageDialogue(entry.text, entry.speakerDisplayName)
    }
}

fun Player.sendMessageDialogue(text: String, speakerDisplayName: String) {
    sendMiniWithResolvers(
        messageFormat,
        Placeholder.parsed("speaker", speakerDisplayName.parsePlaceholders(this)),
        Placeholder.parsed("message", text.parsePlaceholders(this).replace("\n", "\n$messagePadding"))
    )
}