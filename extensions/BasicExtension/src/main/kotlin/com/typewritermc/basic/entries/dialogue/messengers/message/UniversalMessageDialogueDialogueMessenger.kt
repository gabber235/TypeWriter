package com.typewritermc.basic.entries.dialogue.messengers.message

import com.typewritermc.basic.entries.dialogue.MessageDialogueEntry
import com.typewritermc.core.extension.annotations.Messenger
import com.typewritermc.core.interaction.InteractionContext
import com.typewritermc.engine.paper.entry.dialogue.DialogueMessenger
import com.typewritermc.engine.paper.entry.dialogue.MessengerFilter
import com.typewritermc.engine.paper.entry.dialogue.MessengerState
import com.typewritermc.engine.paper.entry.dialogue.TickContext
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.interaction.chatHistory
import com.typewritermc.engine.paper.snippets.snippet
import com.typewritermc.engine.paper.utils.sendMiniWithResolvers
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

val messageFormat: String by snippet(
    "dialogue.message.format",
    "\n<gray> [ <bold><speaker></bold><reset><gray> ]\n<reset><white> <message>\n"
)

val messagePadding: String by snippet(
    "dialogue.message.padding",
    " "
)

@Messenger(MessageDialogueEntry::class)
class UniversalMessageDialogueDialogueMessenger(
    player: Player,
    context: InteractionContext,
    entry: MessageDialogueEntry
) :
    DialogueMessenger<MessageDialogueEntry>(player, context, entry) {

    companion object : MessengerFilter {
        override fun filter(player: Player, entry: DialogueEntry): Boolean = true
    }

    override fun init() {
        super.init()
        // The player might have had something before this. So we want to clean the chat before sending our message.
        player.chatHistory.resendMessages(player)
    }

    override fun tick(context: TickContext) {
        super.tick(context)
        if (state != MessengerState.RUNNING) return
        state = MessengerState.FINISHED
        player.sendMessageDialogue(entry.text.get(player), entry.speakerDisplayName.get(player))
    }

    override fun end() {
        // Do nothing as we don't need to resend the messages.
    }
}

fun Player.sendMessageDialogue(text: String, speakerDisplayName: String) {
    sendMiniWithResolvers(
        messageFormat,
        Placeholder.parsed("speaker", speakerDisplayName.parsePlaceholders(this)),
        Placeholder.parsed("message", text.parsePlaceholders(this).replace("\n", "\n$messagePadding"))
    )
}