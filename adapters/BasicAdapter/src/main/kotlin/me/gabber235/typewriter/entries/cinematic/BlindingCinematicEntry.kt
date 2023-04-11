package me.gabber235.typewriter.entries.cinematic

import com.github.shynixn.mccoroutine.launch
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.utils.*
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.GAME_MODE
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.LOCATION
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
	private val entry: BlindingCinematicEntry,
) : CinematicAction {

	private var previousSegment: BlindingSegment? = null
	private var state: PlayerState? = null

	private fun setupSegment(segment: BlindingSegment) {
		previousSegment = segment
		state = player.state(LOCATION, GAME_MODE)

		plugin.launch {
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

	private fun resetSegment() {
		previousSegment = null
		val state = state ?: return
		this.state = null
		plugin.launch {
			player.removePotionEffect(PotionEffectType.BLINDNESS)
			player.restore(state)
		}
	}

	override fun tick(frame: Int) {
		super.tick(frame)
		val segment = entry.segments activeSegmentAt frame
		
		if (segment == previousSegment) return

		if (previousSegment != null) {
			resetSegment()
		}
		if (segment != null) {
			setupSegment(segment)
		}
	}

	override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}