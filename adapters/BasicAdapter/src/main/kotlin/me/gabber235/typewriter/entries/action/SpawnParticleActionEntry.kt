package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import org.bukkit.Location
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
    val location: Optional<Location> = Optional.empty(),
    @Help("The particle to spawn.")
    val particle: Particle = Particle.SMOKE_NORMAL,
    @Help("The amount of particles to spawn.")
    val count: Int = 1,
    @Help("The offset from the location on the X axis.")
    val offsetX: Double = 0.0,
    @Help("The offset from the location on the Y axis.")
    val offsetY: Double = 0.0,
    @Help("The offset from the location on the Z axis.")
    val offsetZ: Double = 0.0,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        if (location.isPresent) {
            location.get().world?.spawnParticle(particle, location.get(), count, offsetX, offsetY, offsetZ)
        } else {
            player.world.spawnParticle(particle, player.location, count, offsetX, offsetY, offsetZ)
        }

    }
}