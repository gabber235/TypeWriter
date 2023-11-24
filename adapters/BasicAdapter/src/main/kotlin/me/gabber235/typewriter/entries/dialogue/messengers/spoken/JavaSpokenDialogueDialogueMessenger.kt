package me.gabber235.typewriter.entries.dialogue.messengers.spoken

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.SpokenDialogueEntry
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.dialogue.MessengerState
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.asMiniWithResolvers
import me.gabber235.typewriter.utils.asPartialFormattedMini
import me.gabber235.typewriter.utils.toTicks
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerSwapHandItemsEvent
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
		|<next_color>${" ".repeat(20)} Press<white> <key:key.swapOffhand> </white>to <finish_text>
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
val spokenInstructionTicksHighlighted: Int by snippet("dialogue.spoken.instruction.ticks.highlighted", 10)
val spokenInstructionTicksBase: Int by snippet("dialogue.spoken.instruction.ticks.base", 30)


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

        listen<PlayerSwapHandItemsEvent> { event ->
            if (event.player.uniqueId != player.uniqueId) return@listen
            state = MessengerState.FINISHED
            event.isCancelled = true
        }
    }

    override fun tick(cycle: Int) {
        if (state != MessengerState.RUNNING) return
        player.sendSpokenDialogue(entry.text, speakerDisplayName, entry.duration, cycle, triggers.isEmpty())
    }
}

fun Player.sendSpokenDialogue(
    text: String,
    speakerDisplayName: String,
    duration: Duration,
    cycle: Int,
    canFinish: Boolean
) {
    val durationInTicks = duration.toTicks()

    val percentage = (cycle / durationInTicks.toDouble())

    val totalInstructionDuration = spokenInstructionTicksHighlighted + spokenInstructionTicksBase
    val instructionCycle = cycle % totalInstructionDuration
    if (percentage > 1) {
        // Change in highlight color
        val shouldDisplay = instructionCycle == 0 || instructionCycle == spokenInstructionTicksHighlighted
        if (!shouldDisplay) return
    }

    val highlightStarted = cycle > durationInTicks * 2.5
    val needsHighlight = instructionCycle < spokenInstructionTicksHighlighted
    val nextColor =
        if (highlightStarted && needsHighlight) spokenInstructionHighlightColor else spokenInstructionBaseColor

    val continueOrFinish = if (canFinish) spokenInstructionFinishText else spokenInstructionNextText

    val message = text.parsePlaceholders(this).asPartialFormattedMini(
        percentage,
        padding = spokenPadding,
        minLines = spokenMinLines,
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