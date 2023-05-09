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
import org.bukkit.entity.Player

@Entry("actionbar_dialogue_cinematic", "Show an action bar typed dialogue", Colors.CYAN, Icons.XMARKS_LINES)
data class ActionBarDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    @EntryIdentifier(SpeakerEntry::class)
    val speaker: String = "",
    @Segments(icon = Icons.MESSAGE)
    val segments: List<ActionBarDialogueSegment> = emptyList(),
) : CinematicEntry {
    val speakerDisplayName: String
        get() = speakerEntry?.displayName ?: ""

    val speakerEntry: SpeakerEntry?
        get() = Query.findById(speaker)

    override fun create(player: Player): CinematicAction {
        return ActionBarDialogueCinematicAction(
            player,
            this,
        )
    }
}

data class ActionBarDialogueSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("The text to display to the player.")
    val text: String = "",
) : Segment

val actionBarFormat: String by snippet(
    "cinematic.dialogue.actionbar.format",
    "<bold><speaker></bold><reset><gray>: <white><message><padding>"
)
val actionBarPercentage: Double by snippet(
    "cinematic.dialogue.actionbar.percentage",
    0.4
)

class ActionBarDialogueCinematicAction(
    val player: Player,
    val entry: ActionBarDialogueCinematicEntry,
) : CinematicAction {
    private val speakerName = entry.speakerDisplayName
    private var previousSegment: ActionBarDialogueSegment? = null
    private var state: PlayerState? = null

    override suspend fun setup() {
        super.setup()
        state = player.state(GenericPlayerStateProvider.EXP, GenericPlayerStateProvider.LEVEL)
        player.exp = 0f
        player.level = 0
    }

    override suspend fun tick(frame: Int) {
        super.tick(frame)
        val segment = (entry.segments activeSegmentAt frame)

        if (segment == null) {
            if (previousSegment != null) {
                player.exp = 0f
                player.level = 0
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
        val displayPercentage = percentage / actionBarPercentage

        val text = segment.text.parsePlaceholders(player)
        val message = text.asMini()
            .splitPercentage(displayPercentage)
            .color(NamedTextColor.WHITE)

        // Find out how much padding is needed to fill the rest of the action bar.
        // As the action bar is centered, adding padding to the end of the message
        // will make the message appear stationary.
        val paddingSize = text.stripped().length - message.plainText().length
        val padding = " ".repeat(paddingSize)

        val component = actionBarFormat.asMiniWithResolvers(
            Placeholder.parsed("speaker", speakerName),
            Placeholder.component("message", message),
            Placeholder.unparsed("padding", padding),
        )

        player.sendActionBar(component)
    }

    override suspend fun teardown() {
        super.teardown()
        player.restore(state)
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}