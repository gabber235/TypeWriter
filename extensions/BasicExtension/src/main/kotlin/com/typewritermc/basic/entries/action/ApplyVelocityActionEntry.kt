package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.utils.point.Vector
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.utils.toBukkitVector
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
    val force: Vector = Vector(0.0, 0.0, 0.0),
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        player.velocity = player.velocity.add(force.toBukkitVector())
    }
}