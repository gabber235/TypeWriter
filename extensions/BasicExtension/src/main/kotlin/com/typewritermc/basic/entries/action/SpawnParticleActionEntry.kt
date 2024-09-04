package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Negative
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.utils.toBukkitLocation
import org.bukkit.Particle
import org.bukkit.entity.Player
import java.util.*

@Entry("spawn_particles", "Spawn particles at location", Colors.RED, "fa6-solid:fire-flame-simple")
/**
 * The `Spawn Particle Action` is an action that spawns a specific particle at a given location. This action provides you with the ability to spawn particles with a specified type, count, and location.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to create visual effects in response to specific events, such as explosions or magical spells. The possibilities are endless!
 */
class SpawnParticleActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The location to spawn the particles at. (Defaults to player's location)")
    val location: Optional<Position> = Optional.empty(),
    val particle: Particle = Particle.FLAME,
    val count: Int = 1,
    @Negative
    val offsetX: Double = 0.0,
    @Negative
    val offsetY: Double = 0.0,
    @Negative
    val offsetZ: Double = 0.0,
    @Help("The speed of the particles. For some particles, this is the \"extra\" data value to control particle behavior.")
    val speed: Double = 0.0,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        if (location.isPresent) {
            val bukkitLocation = location.get().toBukkitLocation()
            bukkitLocation.world?.spawnParticle(particle, bukkitLocation, count, offsetX, offsetY, offsetZ, speed)
        } else {
            player.world.spawnParticle(particle, player.location, count, offsetX, offsetY, offsetZ, speed)
        }
    }
}