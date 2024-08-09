package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.ThreadType.SYNC
import org.bukkit.Location
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
    @Help("The material of the block to set.")
    val material: Material = Material.AIR,
    @Help("The location to set the block at.")
    val location: Location = Location(null, 0.0, 0.0, 0.0),
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        SYNC.launch {
            location.block.type = material
        }
    }
}