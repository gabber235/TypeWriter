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
import me.gabber235.typewriter.interaction.acceptActionBarMessage
import me.gabber235.typewriter.interaction.startBlockingActionBar
import me.gabber235.typewriter.interaction.stopBlockingActionBar
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
    val segments: List<DisplayDialogueSegment> = emptyList(),
) : CinematicEntry {
    private val speakerEntry: SpeakerEntry?
        get() = Query.findById(speaker)

    override fun create(player: Player): CinematicAction {
        return DisplayDialogueCinematicAction(
            player,
            speakerEntry,
            segments,
            actionBarPercentage,
            setup = { startBlockingActionBar() },
            teardown = { stopBlockingActionBar() },
            reset = { sendActionBar(Component.empty()) },
            display = ::displayActionBar,
        )
    }
}

@Entry(
    "random_actionbar_dialogue_cinematic",
    "Show a random action bar typed dialogue",
    Colors.CYAN,
    Icons.XMARKS_LINES
)
data class RandomActionBarDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    @EntryIdentifier(SpeakerEntry::class)
    val speaker: String = "",
    @Segments(icon = Icons.MESSAGE)
    val segments: List<RandomDisplayDialogueSegment> = emptyList(),
) : CinematicEntry {
    private val speakerEntry: SpeakerEntry?
        get() = Query.findById(speaker)

    override fun create(player: Player): CinematicAction {
        return DisplayDialogueCinematicAction(
            player,
            speakerEntry,
            segments.toDisplaySegments(),
            actionBarPercentage,
            setup = { startBlockingActionBar() },
            teardown = { stopBlockingActionBar() },
            reset = { sendActionBar(Component.empty()) },
            display = ::displayActionBar,
        )
    }
}


val actionBarFormat: String by snippet(
    "cinematic.dialogue.actionbar.format",
    "<bold><speaker></bold><reset><gray>: <white><message><padding>"
)
val actionBarPercentage: Double by snippet(
    "cinematic.dialogue.actionbar.percentage",
    0.4
)

private fun displayActionBar(player: Player, speakerName: String, text: String, displayPercentage: Double) {
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

    player.acceptActionBarMessage(component)
    player.sendActionBar(component)
}
