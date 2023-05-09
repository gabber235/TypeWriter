package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.dialogue.playSpeakerSound
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.entries.SystemTrigger.DIALOGUE_END
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.*
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.EXP
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.LEVEL
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

@Entry("spoken_dialogue_cinematic", "Play a spoken dialogue cinematic", Colors.CYAN, Icons.MESSAGE)
data class SpokenDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    @EntryIdentifier(SpeakerEntry::class)
    val speaker: String = "",
    @Segments(icon = Icons.MESSAGE)
    val segments: List<SpokenDialogueSegment> = emptyList(),
) : CinematicEntry {
    val speakerDisplayName: String
        get() = speakerEntry?.displayName ?: ""

    val speakerEntry: SpeakerEntry?
        get() = Query.findById(speaker)

    override fun create(player: Player): CinematicAction {
        return SpokenDialogueCinematicAction(
            player,
            this,
        )
    }
}

data class SpokenDialogueSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("The text to display to the player.")
    val text: String = "",
) : Segment

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

class SpokenDialogueCinematicAction(
    private val player: Player,
    private val entry: SpokenDialogueCinematicEntry,
) : CinematicAction {

    private val speakerName = entry.speakerDisplayName
    private var previousSegment: SpokenDialogueSegment? = null

    private var state: PlayerState? = null

    override suspend fun setup() {
        super.setup()
        state = player.state(EXP, LEVEL)
        player.exp = 0f
        player.level = 0
        // If the player is already in a dialogue, end it in favor of this one.
        DIALOGUE_END triggerFor player
    }


    override suspend fun tick(frame: Int) {
        super.tick(frame)
        val segment = (entry.segments activeSegmentAt frame)

        if (segment == null) {
            if (previousSegment != null) {
                player.exp = 0f
                player.chatHistory.resendMessages(player)
                previousSegment = null
            }
            return
        }

        if (previousSegment != segment) {
            player.exp = 1f
            player.playSpeakerSound(entry.speakerEntry)
            previousSegment = segment
        }

        val percentage = segment percentageAt frame
        player.exp = 1 - percentage.toFloat()

        // The percentage of the dialogue that should be displayed.
        val displayPercentage = percentage / spokenPercentage

        val message = segment.text.parsePlaceholders(player).asPartialFormattedMini(
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

        val componentWithDarkMessages = player.chatHistory.composeDarkMessage(component)
        player.sendMessage(componentWithDarkMessages)
    }

    override suspend fun teardown() {
        super.teardown()
        player.restore(state)
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}

