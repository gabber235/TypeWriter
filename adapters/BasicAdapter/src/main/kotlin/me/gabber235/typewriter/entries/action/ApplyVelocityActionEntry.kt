package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Vector
import org.bukkit.entity.Player

@Entry("apply_velocity", "Apply a velocity to the player", Colors.RED, "fa-solid:wind")
/**
 * The `ApplyVelocityActionEntry` is an action that applies a velocity to the player.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to simulate wind, explosions, or any other force that would move the player.
 */
class ApplyVelocityActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The force to apply to the player.")
    val force: Vector = Vector(0.0, 0.0, 0.0),
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        player.velocity = player.velocity.add(force.toBukkitVector())
    }
}