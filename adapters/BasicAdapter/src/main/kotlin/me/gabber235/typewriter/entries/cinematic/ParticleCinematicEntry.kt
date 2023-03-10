package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Segments
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
	val location: Location = Location(null, 0.0, 0.0, 0.0),
	val particle: Particle = Particle.FLAME,
	val offsetX: Double = 0.0,
	val offsetY: Double = 0.0,
	val offsetZ: Double = 0.0,
	val speed: Double = 0.0,
	val spawnCountPerTick: Int = 0,
	@Segments(icon = Icons.FIRE_FLAME_SIMPLE)
	val segments: List<ParticleSegment> = emptyList(),
) : CinematicEntry {
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
) : Segment

class ParticleCinematicAction(
	private val player: Player,
	private val entry: ParticleCinematicEntry,
) : CinematicAction {
	override fun tick(frame: Int) {
		super.tick(frame)
		(entry.segments activeSegmentAt frame) ?: return

		player.spawnParticle(
			entry.particle,
			entry.location,
			entry.spawnCountPerTick,
			entry.offsetX,
			entry.offsetY,
			entry.offsetZ,
			entry.speed
		)

	}

	override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}
