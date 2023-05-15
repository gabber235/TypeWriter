package me.gabber235.typewriter.entries.cinematic

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.withContext
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.GAME_MODE
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.LOCATION
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.PlayerState
import me.gabber235.typewriter.utils.restore
import me.gabber235.typewriter.utils.state
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Entry("blinding_cinematic", "Blind the player so the screen looks black", Colors.CYAN, Icons.SOLID_EYE_SLASH)
class BlindingCinematicEntry(
    override val id: String,
    override val name: String,
    override val criteria: List<Criteria>,
    @Segments(icon = Icons.SOLID_EYE_SLASH)
    val segments: List<BlindingSegment>,
) : CinematicEntry {
    override fun shouldSimulate(): Boolean = false
    override fun create(player: Player): CinematicAction {
        return BlindingCinematicAction(
            player,
            this,
        )
    }
}

data class BlindingSegment(
    override val startFrame: Int,
    override val endFrame: Int,
    @Help("Teleport the player to y 500 to prevent them from seeing the world")
    val teleport: Boolean = false,
    @Help("Set the player to spectator mode")
    val spectator: Boolean = false,
) : Segment

class BlindingCinematicAction(
    private val player: Player,
    entry: BlindingCinematicEntry,
) : SimpleCinematicAction<BlindingSegment>() {

    private var state: PlayerState? = null

    override val segments: List<BlindingSegment> = entry.segments

    override suspend fun startSegment(segment: BlindingSegment) {
        super.startSegment(segment)
        state = player.state(LOCATION, GAME_MODE)

        withContext(plugin.minecraftDispatcher) {
            player.addPotionEffect(PotionEffect(PotionEffectType.BLINDNESS, 1000000, 1, false, false, false))

            if (segment.teleport) {
                /// Teleport the player to y 500 to prevent them from seeing the world
                val location = player.location.clone()
                location.y = 500.0
                player.teleport(location)
            }
            if (segment.spectator) {
                player.gameMode = GameMode.SPECTATOR
            }
        }
    }

    override suspend fun stopSegment(segment: BlindingSegment) {
        super.stopSegment(segment)
        val state = state ?: return
        this.state = null
        withContext(plugin.minecraftDispatcher) {
            player.removePotionEffect(PotionEffectType.BLINDNESS)
            player.restore(state)
        }
    }

}