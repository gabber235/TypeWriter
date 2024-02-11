package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.interaction.acceptActionBarMessage
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player

@Entry("actionbar_dialogue_cinematic", "Show an action bar typed dialogue", Colors.CYAN, "fa6-solid:xmarks-lines")
/**
 * The `Action Bar Dialogue Cinematic` is a cinematic that shows a dialogue in the action bar.
 * You can specify the speaker and the dialogue.
 *
 * ## How could this be used?
 *
 * This cinematic is useful to display dialogue in combination with a camera path.
 * As the dialogue is displayed in the action bar, the player can still move around and look at the camera path.
 */
class ActionBarDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    val speaker: Ref<SpeakerEntry> = emptyRef(),
    @Segments(icon = "fa6-solid:xmarks-lines")
    val segments: List<DisplayDialogueSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return DisplayDialogueCinematicAction(
            player,
            speaker.get(),
            segments,
            actionBarPercentage,
            reset = { sendActionBar(Component.empty()) },
            display = ::displayActionBar,
        )
    }
}

@Entry(
    "random_actionbar_dialogue_cinematic",
    "Show a random action bar typed dialogue",
    Colors.CYAN,
    "fa6-solid:xmarks-lines"
)
data class RandomActionBarDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    val speaker: Ref<SpeakerEntry> = emptyRef(),
    @Segments(icon = "fa6-solid:xmarks-lines")
    val segments: List<RandomDisplayDialogueSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return DisplayDialogueCinematicAction(
            player,
            speaker.get(),
            segments.toDisplaySegments(),
            actionBarPercentage,
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
