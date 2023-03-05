package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import java.time.Duration

@Entry("particle_cinematic", "Spawn particles for a cinematic", Colors.CYAN, Icons.FIRE_FLAME_SIMPLE)
data class ParticleCinematicEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	val location: Location = Location(null, 0.0, 0.0, 0.0),
	val particle: Particle = Particle.FLAME,
	val offsetX: Double = 0.0,
	val offsetY: Double = 0.0,
	val offsetZ: Double = 0.0,
	val speed: Double = 0.0,
	val spawnCountPerTick: Int = 0,
	val duration: Duration = Duration.ZERO,
) : CinematicEntry {
	override fun create(player: Player): CinematicAction {
		return ParticleCinematicAction(
			player,
			location,
			particle,
			offsetX,
			offsetY,
			offsetZ,
			speed,
			spawnCountPerTick,
			duration
		)
	}
}

class ParticleCinematicAction(
	private val player: Player,
	private val location: Location,
	private val particle: Particle,
	private val offsetX: Double,
	private val offsetY: Double,
	private val offsetZ: Double,
	private val speed: Double,
	private val spawnCountPerTick: Int,
	private val duration: Duration,
) : CinematicAction {
	private var elapsed = Duration.ZERO
	
	override fun tick(delta: Duration) {
		elapsed += delta

		player.spawnParticle(
			particle,
			location,
			spawnCountPerTick,
			offsetX,
			offsetY,
			offsetZ,
			speed
		)
		super.tick(delta)
	}

	override val canFinish: Boolean
		get() = elapsed >= duration
}
