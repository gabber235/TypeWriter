package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.dialogue.playSpeakerSound
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.GenericPlayerStateProvider
import me.gabber235.typewriter.utils.PlayerState
import me.gabber235.typewriter.utils.restore
import me.gabber235.typewriter.utils.state
import org.bukkit.entity.Player

data class DisplayDialogueSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("The text to display to the player.")
    val text: String = "",
) : Segment

data class RandomDisplayDialogueSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("Possible texts to display to the player.")
    val texts: List<String> = emptyList(),
) : Segment {
    fun toDisplaySegment(): DisplayDialogueSegment {
        return DisplayDialogueSegment(startFrame, endFrame, texts.random())
    }
}

fun List<RandomDisplayDialogueSegment>.toDisplaySegments(): List<DisplayDialogueSegment> {
    return map { it.toDisplaySegment() }
}


class DisplayDialogueCinematicAction(
    val player: Player,
    val speaker: SpeakerEntry?,
    private val segments: List<DisplayDialogueSegment>,
    private val splitPercentage: Double,
    private val setup: (Player.() -> Unit)? = null,
    private val teardown: (Player.() -> Unit)? = null,
    private val reset: (Player.() -> Unit)? = null,
    val display: (Player, String, String, Double) -> Unit,
) : CinematicAction {
    private var previousSegment: DisplayDialogueSegment? = null
    private var state: PlayerState? = null

    override suspend fun setup() {
        super.setup()
        state = player.state(GenericPlayerStateProvider.EXP, GenericPlayerStateProvider.LEVEL)
        player.exp = 0f
        player.level = 0
        setup?.invoke(player)
    }

    override suspend fun tick(frame: Int) {
        super.tick(frame)
        val segment = (segments activeSegmentAt frame)

        if (segment == null) {
            if (previousSegment != null) {
                player.exp = 0f
                player.level = 0
                reset?.invoke(player)
                previousSegment = null
            }
            return
        }

        if (previousSegment != segment) {
            player.exp = 1f
            player.playSpeakerSound(speaker)
            previousSegment = segment
        }

        val percentage = segment percentageAt frame
        player.exp = 1 - percentage.toFloat()

        // The percentage of the dialogue that should be displayed.
        val displayPercentage = percentage / splitPercentage

        val text = segment.text.parsePlaceholders(player)

        display(player, speaker?.displayName ?: "", text, displayPercentage)
    }

    override suspend fun teardown() {
        super.teardown()
        player.restore(state)
        teardown?.invoke(player)
        reset?.invoke(player)
    }

    override fun canFinish(frame: Int): Boolean = segments canFinishAt frame
}