package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_END
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.interaction.acceptActionBarMessage
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.asMiniWithResolvers
import me.gabber235.typewriter.utils.asPartialFormattedMini
import me.gabber235.typewriter.utils.isFloodgate
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

@Entry("spoken_dialogue_cinematic", "Play a spoken dialogue cinematic", Colors.CYAN, Icons.MESSAGE)
/**
 * The `Spoken Dialogue Cinematic` is a cinematic that displays an animated message in chat.
 *
 * ## How could this be used?
 *
 * When a NPC is talking to the player, this can be used to display the NPC's dialogue.
 */
class SpokenDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    @EntryIdentifier(SpeakerEntry::class)
    val speaker: String = "",
    @Segments(icon = Icons.MESSAGE)
    val segments: List<DisplayDialogueSegment> = emptyList(),
) : CinematicEntry {
    val speakerEntry: SpeakerEntry?
        get() = Query.findById(speaker)

    override fun create(player: Player): CinematicAction {
        return DisplayDialogueCinematicAction(
            player,
            speakerEntry,
            segments,
            spokenPercentage,
            setup = {
                // If the player is already in a dialogue, end it in favor of this one.
                DIALOGUE_END.triggerFor(this)
            },
            reset = { chatHistory.resendMessages(this) },
            display = ::displaySpokenDialogue,
        )
    }
}

@Entry("random_spoken_dialogue_cinematic", "Play a random spoken dialogue cinematic", Colors.CYAN, Icons.MESSAGE)
data class RandomSpokenDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    @EntryIdentifier(SpeakerEntry::class)
    val speaker: String = "",
    @Segments(icon = Icons.MESSAGE)
    val segments: List<RandomDisplayDialogueSegment> = emptyList(),
) : CinematicEntry {
    val speakerEntry: SpeakerEntry?
        get() = Query.findById(speaker)

    override fun create(player: Player): CinematicAction {
        return DisplayDialogueCinematicAction(
            player,
            speakerEntry,
            segments.toDisplaySegments(),
            spokenPercentage,
            setup = {
                // If the player is already in a dialogue, end it in favor of this one.
                DIALOGUE_END.triggerFor(this)
            },
            reset = reset@{
                if (isFloodgate) return@reset
                chatHistory.resendMessages(this)
            },
            display = ::displaySpokenDialogue,
        )
    }
}


val spokenFormat: String by snippet(
    "cinematic.dialogue.spoken.format",
    """
		|<gray><st>${" ".repeat(60)}</st>
		|
		|<gray><padding>[ <bold><speaker></bold><reset><gray> ]
		|
		|<message>
		|
		|<gray><st>${" ".repeat(60)}</st>
		""".trimMargin()
)

val spokenPadding: String by snippet("cinematic.dialogue.spoken.padding", "    ")
val spokenMinLines: Int by snippet("cinematic.dialogue.spoken.minLines", 4)
val spokenMaxLineLength: Int by snippet("cinematic.dialogue.spoken.maxLineLength", 40)
val spokenPercentage: Double by snippet("cinematic.dialogue.spoken.percentage", 0.5)

private fun displaySpokenDialogue(player: Player, speakerName: String, text: String, displayPercentage: Double) {

    val message = text.asPartialFormattedMini(
        displayPercentage,
        padding = spokenPadding,
        minLines = spokenMinLines,
        maxLineLength = spokenMaxLineLength,
    )

    val component = spokenFormat.asMiniWithResolvers(
        Placeholder.parsed("speaker", speakerName),
        Placeholder.component("message", message),
        Placeholder.parsed("padding", spokenPadding),
    )

    // Bedrock clients don't like chat animations, but they can have multiline actionbars.
    if (player.isFloodgate) {
        player.acceptActionBarMessage(component)
        player.sendActionBar(component)
        return
    }

    val componentWithDarkMessages = player.chatHistory.composeDarkMessage(component)
    player.sendMessage(componentWithDarkMessages)
}