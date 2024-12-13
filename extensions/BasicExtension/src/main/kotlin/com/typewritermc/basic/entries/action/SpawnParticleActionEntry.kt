package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.utils.point.Position
import com.typewritermc.core.utils.point.Vector
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var
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
    val location: Optional<Var<Position>> = Optional.empty(),
    val particle: Var<Particle> = ConstVar(Particle.FLAME),
    val count: Var<Int> = ConstVar(1),
    val offset: Var<Vector> = ConstVar(Vector.ZERO),
    @Help("The speed of the particles. For some particles, this is the \"extra\" data value to control particle behavior.")
    val speed: Var<Double> = ConstVar(0.0),
) : ActionEntry {
    override fun ActionTrigger.execute() {
        if (location.isPresent) {
            val bukkitLocation = location.get().get(player, context).toBukkitLocation()
            bukkitLocation.world?.spawnParticle(
                particle.get(player, context),
                bukkitLocation,
                count.get(player, context),
                offset.get(player, context).x,
                offset.get(player, context).y,
                offset.get(player, context).z,
                speed.get(player, context)
            )
        } else {
            player.world.spawnParticle(
                particle.get(player, context),
                player.location,
                count.get(player, context),
                offset.get(player, context).x,
                offset.get(player, context).y,
                offset.get(player, context).z,
                speed.get(player, context)
            )
        }
    }
}