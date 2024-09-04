package com.typewritermc.basic.entries.cinematic

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Negative
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.utils.toBukkitLocation
import org.bukkit.Particle
import org.bukkit.entity.Player

@Entry("particle_cinematic", "Spawn particles for a cinematic", Colors.CYAN, "fa6-solid:fire-flame-simple")
/**
 * The `Particle Cinematic` entry is used to spawn particles for a cinematic.
 *
 * ## How could this be used?
 *
 * This can be used to add dramatic effects to a cinematic.
 * Like, blowing up a building and spawning a bunch of particles.
 * Or, adding focus to a certain area by spawning particles around it.
 */
class ParticleCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    val location: Position = Position.ORIGIN,
    val particle: Particle = Particle.FLAME,
    @Help("The amount of particles to spawn every tick.")
    val count: Int = 1,
    @Negative
    val offsetX: Double = 0.0,
    @Negative
    val offsetY: Double = 0.0,
    @Negative
    val offsetZ: Double = 0.0,
    @Help("The speed of the particles. For some particles, this is the \"extra\" data value to control particle behavior.")
    val speed: Double = 0.0,
    @Segments(icon = "fa6-solid:fire-flame-simple")
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
    override suspend fun tick(frame: Int) {
        super.tick(frame)
        (entry.segments activeSegmentAt frame) ?: return

        player.spawnParticle(
            entry.particle,
            entry.location.toBukkitLocation(),
            entry.count,
            entry.offsetX,
            entry.offsetY,
            entry.offsetZ,
            entry.speed
        )

    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}
