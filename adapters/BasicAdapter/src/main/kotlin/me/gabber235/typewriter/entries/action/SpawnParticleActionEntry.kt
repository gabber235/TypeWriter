package me.gabber235.typewriter.entries.action

import lirand.api.extensions.server.commands.dispatchCommand
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import java.util.*

@Entry("spawn_particles", "Spawn particles at location", Colors.RED, Icons.FIRE_FLAME_SIMPLE)
data class SpawnParticleActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    val location: Optional<Location> = Optional.empty(),
    val particle: Particle = Particle.SMOKE_NORMAL,
    val count: Int = 1,
    val offsetX: Double = 0.0,
    val offsetY: Double = 0.0,
    val offsetZ: Double = 0.0,

) : ActionEntry {

    override fun execute(player: Player) {
        super.execute(player)

        if(location.isPresent) {
            location.get().world?.spawnParticle(particle, location.get(), count, offsetX, offsetY, offsetZ)
        } else {
            player.world.spawnParticle(particle, player.location, count, offsetX, offsetY, offsetZ)
        }

    }




}