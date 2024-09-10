package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import com.typewritermc.engine.paper.utils.toBukkitLocation
import org.bukkit.Material
import org.bukkit.entity.Player

@Entry("set_block", "Set a block at a location", Colors.RED, "fluent:cube-add-20-filled")
/**
 * The `SetBlockActionEntry` is an action that sets a block at a specific location.
 *
 * :::caution
 * This will set the block for all the players on the server, not just the player who triggered the action.
 * It will modify the world, so be careful when using this action.
 * :::
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to create structures, set traps, or any other custom block placements you want to make. The possibilities are endless!
 */
class SetBlockActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val material: Material = Material.AIR,
    val location: Position = Position.ORIGIN,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        SYNC.launch {
            val bukkitLocation = location.toBukkitLocation()
            bukkitLocation.block.type = material
        }
    }
}