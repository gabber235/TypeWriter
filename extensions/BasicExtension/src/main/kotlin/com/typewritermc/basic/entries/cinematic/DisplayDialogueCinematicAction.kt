package com.typewritermc.basic.entries.cinematic

import com.typewritermc.core.extension.annotations.Colored
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.MultiLine
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.engine.paper.entry.dialogue.playSpeakerSound
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.utils.GenericPlayerStateProvider.EXP
import com.typewritermc.engine.paper.utils.GenericPlayerStateProvider.LEVEL
import com.typewritermc.engine.paper.utils.PlayerState
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.engine.paper.utils.restore
import com.typewritermc.engine.paper.utils.state
import org.bukkit.entity.Player

data class SingleLineDisplayDialogueSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    override val text: String = "",
) : DisplayDialogueSegment

data class MultiLineDisplayDialogueSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @MultiLine
    override val text: String = "",
) : DisplayDialogueSegment

interface DisplayDialogueSegment : Segment {
    @Placeholder
    @Colored
    @Help("The text to display to the player.")
    val text: String
}

data class SingleLineRandomDisplayDialogueSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    override val texts: List<String> = emptyList(),
): RandomDisplayDialogueSegment {
    override fun toDisplaySegment(): DisplayDialogueSegment {
        return SingleLineDisplayDialogueSegment(startFrame, endFrame, texts.random())
    }
}

data class MultiLineRandomDisplayDialogueSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @MultiLine
    override val texts: List<String> = emptyList(),
): RandomDisplayDialogueSegment {
    override fun toDisplaySegment(): DisplayDialogueSegment {
        return MultiLineDisplayDialogueSegment(startFrame, endFrame, texts.random())
    }
}

interface RandomDisplayDialogueSegment : Segment {
    @Help("One of the possible texts is chosen randomly, and displayed to the player.")
    val texts: List<String>

    fun toDisplaySegment(): DisplayDialogueSegment
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
        state = player.state(EXP, LEVEL)
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

        // We must make sure that the full dialogue is displayed before we stop spamming it.
        if (displayPercentage > 1.1) {
            // When the dialogue is fully displayed, we don't need to display it every tick and should avoid spamming the player.
            val needsDisplay = (frame - segment.startFrame) % 20 == 0
            if (!needsDisplay) return
        }

        val text = segment.text.parsePlaceholders(player)

        display(player, speaker?.displayName?.parsePlaceholders(player) ?: "", text, displayPercentage)
    }

    override suspend fun teardown() {
        super.teardown()
        teardown?.invoke(player)
        reset?.invoke(player)
        SYNC.switchContext {
            player.restore(state)
        }
    }

    override fun canFinish(frame: Int): Boolean = segments canFinishAt frame
}