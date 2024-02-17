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
import me.gabber235.typewriter.utils.asMini
import me.gabber235.typewriter.utils.asMiniWithResolvers
import me.gabber235.typewriter.utils.splitPercentage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.Times
import org.bukkit.entity.Player
import java.time.Duration

@Entry("subtitle_dialogue_cinematic", "Show an subtitle message", Colors.CYAN, "fa6-solid:diagram-next")
/**
 * The `Subtitle Dialogue Cinematic Entry` is a cinematic entry that displays an animated subtitle message.
 * The speaker is displayed in the action bar, and the dialogue is displayed in the subtitle.
 *
 * ## How could this be used?
 *
 * This could be used to display a dialogue between two characters, where the speaker is displayed in the action bar, and the dialogue is displayed in the subtitle.
 */
class SubtitleDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    val speaker: Ref<SpeakerEntry> = emptyRef(),
    @Segments(icon = "fa6-solid:diagram-next")
    val segments: List<DisplayDialogueSegment> = emptyList(),
) : CinematicEntry {

    override fun create(player: Player): CinematicAction {
        return DisplayDialogueCinematicAction(
            player,
            speaker.get(),
            segments,
            subtitlePercentage,
            reset = {
                sendActionBar(Component.empty())
                player.clearTitle()
            },
            display = ::displaySubTitle
        )
    }
}

@Entry("random_subtitle_dialogue_cinematic", "Show a random action bar message", Colors.CYAN, "fa6-solid:diagram-next")
data class RandomSubtitleDialogueCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Help("The speaker of the dialogue")
    val speaker: Ref<SpeakerEntry> = emptyRef(),
    @Segments(icon = "fa6-solid:diagram-next")
    val segments: List<RandomDisplayDialogueSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return DisplayDialogueCinematicAction(
            player,
            speaker.get(),
            segments.toDisplaySegments(),
            subtitlePercentage,
            reset = {
                sendActionBar(Component.empty())
                player.clearTitle()
            },
            display = ::displaySubTitle
        )
    }
}

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

private fun displaySubTitle(player: Player, speakerName: String, text: String, displayPercentage: Double) {
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
    player.acceptActionBarMessage(actionBarComponent)
    player.sendActionBar(actionBarComponent)
}