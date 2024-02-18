package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.utils.EffectStateProvider
import me.gabber235.typewriter.utils.PlayerState
import me.gabber235.typewriter.utils.ThreadType.SYNC
import me.gabber235.typewriter.utils.restore
import me.gabber235.typewriter.utils.state
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
) : CinematicEntry {
    override fun createSimulated(player: Player): CinematicAction? = null
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
    @Help("The type of potion effect to apply")
    val potionEffectType: PotionEffectType = PotionEffectType.BLINDNESS,
    @Help("The strength of the potion effect")
    val strength: Int = 1,
    @Help("Whether the potion effect should be ambient")
    val ambient: Boolean = false,
    @Help("Whether the potion effect should have particles")
    val particles: Boolean = false,
    @Help("Whether the potion effect should display an icon")
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
