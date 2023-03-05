package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player

@Entry("particle_cinematic", "Spawn particles for a cinematic", Colors.CYAN, Icons.FIRE_FLAME_SIMPLE)
data class ParticleCinematicEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val segments: List<ParticleSegment> = emptyList(),
) : CinematicEntry<ParticleSegment> {
	override fun create(player: Player): CinematicAction {
		return ParticleCinematicAction(
			player,
			this,
		)
	}
}


data class ParticleSegment(
	override val startFrame: Int = 0,
	override val endFrame: Int = 0,
	val location: Location = Location(null, 0.0, 0.0, 0.0),
	val particle: Particle = Particle.FLAME,
	val offsetX: Double = 0.0,
	val offsetY: Double = 0.0,
	val offsetZ: Double = 0.0,
	val speed: Double = 0.0,
	val spawnCountPerTick: Int = 0,
) : Segment

class ParticleCinematicAction(
	private val player: Player,
	private val entry: ParticleCinematicEntry,
) : CinematicAction {
	override fun tick(frame: Int) {
		val segments = entry activeSegmentsAt frame

		segments.forEach { segment ->
			player.spawnParticle(
				segment.particle,
				segment.location,
				segment.spawnCountPerTick,
				segment.offsetX,
				segment.offsetY,
				segment.offsetZ,
				segment.speed
			)
		}

		super.tick(frame)
	}

	override fun canFinish(frame: Int): Boolean = entry canFinishAt frame
}
