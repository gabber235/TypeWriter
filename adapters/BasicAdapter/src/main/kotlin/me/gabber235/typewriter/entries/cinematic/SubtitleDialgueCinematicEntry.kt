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
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.entity.Player
import java.time.Duration

@Entry("subtitle_dialogue_cinematic", "Show an action bar message", Colors.CYAN, Icons.DIAGRAM_NEXT)
data class SubtitleDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    @EntryIdentifier(SpeakerEntry::class)
    val speaker: String = "",
    @Segments(icon = Icons.MESSAGE)
    val segments: List<SubtitleDialogueSegment> = emptyList(),
) : CinematicEntry {
    val speakerDisplayName: String
        get() = speakerEntry?.displayName ?: ""

    val speakerEntry: SpeakerEntry?
        get() = Query.findById(speaker)

    override fun create(player: Player): CinematicAction {
        return SubtitleDialogueCinematicAction(
            player,
            this,
        )
    }
}

data class SubtitleDialogueSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("The text to display to the player.")
    val text: String = "",
) : Segment

val subtitleFormat: String by snippet(
    "cinematic.dialogue.subtitle.format",
    "<white><message>"
)
val subtitleSpeakerFormat: String by snippet(
    "cinematic.dialogue.subtitle.speaker.format",
    "<gray>[ <reset><bold><speaker></bold><reset><gray> ]"
)
val subtitlePercentage: Double by snippet(
    "cinematic.dialogue.subtitle.percentage",
    0.4
)

private val times = Times.times(Duration.ZERO, Duration.ofDays(1), Duration.ZERO)

class SubtitleDialogueCinematicAction(
    val player: Player,
    val entry: SubtitleDialogueCinematicEntry,
) : CinematicAction {
    private val speakerName = entry.speakerDisplayName
    private var previousSegment: SubtitleDialogueSegment? = null

    private var state: PlayerState? = null

    override fun setup() {
        super.setup()
        state = player.state(GenericPlayerStateProvider.EXP, GenericPlayerStateProvider.LEVEL)
        player.exp = 0f
        player.level = 0
    }

    override fun tick(frame: Int) {
        super.tick(frame)
        val segment = (entry.segments activeSegmentAt frame)

        if (segment == null) {
            if (previousSegment != null) {
                player.exp = 0f
                player.showTitle(Title.title(Component.empty(), Component.empty(), times))
                player.sendActionBar(Component.empty())
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
        val displayPercentage = percentage / subtitlePercentage

        val text = segment.text.parsePlaceholders(player)
        val message = text.asMini()
            .splitPercentage(displayPercentage)
            .color(NamedTextColor.WHITE)

        val component = subtitleFormat.asMiniWithResolvers(
            Placeholder.component("message", message),
        )

        val actionBarComponent = subtitleSpeakerFormat.asMiniWithResolvers(
            Placeholder.parsed("speaker", speakerName),
        )

        player.showTitle(Title.title(Component.empty(), component, times))
        player.sendActionBar(actionBarComponent)
    }

    override fun teardown() {
        super.teardown()
        player.restore(state)
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}