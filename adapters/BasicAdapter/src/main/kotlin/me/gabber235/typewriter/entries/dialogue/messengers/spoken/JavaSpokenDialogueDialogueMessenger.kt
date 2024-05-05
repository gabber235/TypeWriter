package me.gabber235.typewriter.entries.dialogue.messengers.spoken

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.SpokenDialogueEntry
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.dialogue.MessengerState
import me.gabber235.typewriter.entry.dialogue.confirmationKey
import me.gabber235.typewriter.entry.dialogue.typingDurationType
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import java.time.Duration

val spokenFormat: String by snippet(
    "dialogue.spoken.format",
    """
		|<gray><st>${" ".repeat(60)}</st>
		|
		|<gray><padding>[ <bold><speaker></bold><reset><gray> ]
		|
		|<message>
		|
		|<next_color>${" ".repeat(20)} Press<white> <confirmation_key> </white>to <finish_text>
		|<gray><st>${" ".repeat(60)}</st>
		""".trimMargin()
)

val spokenInstructionNextText: String by snippet("dialogue.spoken.instruction.next", "continue")
val spokenInstructionFinishText: String by snippet("dialogue.spoken.instruction.finish", "finish")
val spokenInstructionBaseColor: String by snippet("dialogue.spoken.instruction.color.base", "<gray>")
val spokenInstructionHighlightColor: String by snippet("dialogue.spoken.instruction.color.highlight", "<red>")
val spokenPadding: String by snippet("dialogue.spoken.padding", "    ")
val spokenMinLines: Int by snippet("dialogue.spoken.minLines", 3)
val spokenMaxLineLength: Int by snippet("dialogue.spoken.maxLineLength", 40)
val spokenInstructionTicksHighlighted: Long by snippet("dialogue.spoken.instruction.ticks.highlighted", 10)
val spokenInstructionTicksBase: Long by snippet("dialogue.spoken.instruction.ticks.base", 30)


@Messenger(SpokenDialogueEntry::class)
class JavaSpokenDialogueDialogueMessenger(player: Player, entry: SpokenDialogueEntry) :
    DialogueMessenger<SpokenDialogueEntry>(player, entry) {

    companion object : MessengerFilter {
        override fun filter(player: Player, entry: DialogueEntry): Boolean = true
    }

    private var speakerDisplayName = ""
    override fun init() {
        super.init()
        speakerDisplayName = entry.speakerDisplayName

        confirmationKey.listen(this, player.uniqueId) {
            state = MessengerState.FINISHED
        }
    }

    override fun tick(playTime: Duration) {
        if (state != MessengerState.RUNNING) return
        player.sendSpokenDialogue(entry.text, speakerDisplayName, entry.duration, playTime, triggers.isEmpty())
    }
}

fun Player.sendSpokenDialogue(
    text: String,
    speakerDisplayName: String,
    duration: Duration,
    playTime: Duration,
    canFinish: Boolean
) {
    val rawText = text.parsePlaceholders(this).stripped()
    val playedTicks = playTime.toTicks()
    val durationInTicks = typingDurationType.totalDuration(rawText, duration).toTicks()

    val percentage = typingDurationType.calculatePercentage(playTime, duration, rawText)

    val totalInstructionDuration = spokenInstructionTicksHighlighted + spokenInstructionTicksBase
    val instructionCycle = playedTicks % totalInstructionDuration
    // When the messages is send we don't want to keep sending the message every tick.
    // However, we only start reducing after the message has been displayed fully.
    if (percentage > 1.1) {
        // Change in highlight color
        val shouldDisplay = instructionCycle == 0L || instructionCycle == spokenInstructionTicksHighlighted
        if (!shouldDisplay) return
    }

    val highlightStarted = playedTicks > durationInTicks * 2.5
    val needsHighlight = instructionCycle < spokenInstructionTicksHighlighted
    val nextColor =
        if (highlightStarted && needsHighlight) spokenInstructionHighlightColor else spokenInstructionBaseColor

    val continueOrFinish = if (canFinish) spokenInstructionFinishText else spokenInstructionNextText

    val resultingLines = rawText.limitLineLength(spokenMaxLineLength).lineCount

    val message = text.parsePlaceholders(this).asPartialFormattedMini(
        percentage,
        padding = spokenPadding,
        minLines = spokenMinLines.coerceAtLeast(resultingLines),
        maxLineLength = spokenMaxLineLength
    )

    val component = spokenFormat.asMiniWithResolvers(
        Placeholder.parsed("speaker", speakerDisplayName.parsePlaceholders(this)),
        Placeholder.component("message", message),
        Placeholder.parsed("next_color", nextColor),
        Placeholder.parsed("finish_text", continueOrFinish),
        Placeholder.parsed("padding", spokenPadding)
    )

    val componentWithDarkMessages = chatHistory.composeDarkMessage(component)
    sendMessage(componentWithDarkMessages)
}