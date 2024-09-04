package com.typewritermc.basic.entries.cinematic

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.cinematic.SimpleCinematicAction
import com.typewritermc.engine.paper.entry.entries.CinematicAction
import com.typewritermc.engine.paper.entry.entries.PrimaryCinematicEntry
import com.typewritermc.engine.paper.entry.entries.Segment
import com.typewritermc.engine.paper.utils.EffectStateProvider
import com.typewritermc.engine.paper.utils.PlayerState
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.engine.paper.utils.restore
import com.typewritermc.engine.paper.utils.state
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Entry(
    "potion_effect_cinematic",
    "Apply different potion effects to the player during a cinematic",
    Colors.CYAN,
    "fa6-solid:flask-vial"
)
/**
 * The `PotionEffectCinematicEntry` is used to apply different potion effects to the player during a cinematic.
 *
 * ## How could this be used?
 * This can be used to dynamically apply effects like blindness, slowness, etc., at different times
 * during a cinematic, enhancing the storytelling or gameplay experience.
 */
class PotionEffectCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(icon = "heroicons-solid:status-offline")
    val segments: List<PotionEffectSegment> = emptyList()
) : PrimaryCinematicEntry {
    override fun createSimulating(player: Player): CinematicAction? = null
    override fun create(player: Player): CinematicAction {
        return PotionEffectCinematicAction(
            player,
            this
        )
    }
}

data class PotionEffectSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    val potionEffectType: PotionEffectType = PotionEffectType.BLINDNESS,
    @Default("1")
    val strength: Int = 1,
    val ambient: Boolean = false,
    val particles: Boolean = false,
    @Help("Whether the icon should be displayed in the top left corner of the screen.")
    val icon: Boolean = false,
) : Segment

class PotionEffectCinematicAction(
    private val player: Player,
    entry: PotionEffectCinematicEntry
) : SimpleCinematicAction<PotionEffectSegment>() {

    private var state: PlayerState? = null

    override val segments: List<PotionEffectSegment> = entry.segments

    override suspend fun startSegment(segment: PotionEffectSegment) {
        super.startSegment(segment)
        state = player.state(EffectStateProvider(segment.potionEffectType))

        SYNC.switchContext {
            player.addPotionEffect(
                PotionEffect(
                    segment.potionEffectType,
                    10000000,
                    segment.strength,
                    segment.ambient,
                    segment.particles,
                    segment.icon
                )
            )
        }
    }

    override suspend fun stopSegment(segment: PotionEffectSegment) {
        super.stopSegment(segment)
        restoreState()
    }

    private suspend fun restoreState() {
        val state = state ?: return
        this.state = null
        SYNC.switchContext {
            player.restore(state)
        }
    }

    override suspend fun teardown() {
        super.teardown()

        if (state != null) {
            restoreState()
        }
    }
}
