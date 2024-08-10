package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.BLOCK
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.block.BlockPlaceEvent
import java.util.*

@Entry("on_place_block", "When the player places a block", Colors.YELLOW, "fluent:cube-add-20-filled")
/**
 * The `Block Place Event` is called when a block is placed in the world.
 *
 * ## How could this be used?
 *
 * This event could be used to create a custom block that has special properties when placed in the world, like particles or sounds that play. It could also be used to create a block that when placed, can turn itself into a cool structure.
 */
class BlockPlaceEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The location of the block that was placed.")
    val location: Optional<Location> = Optional.empty(),
    @MaterialProperties(BLOCK)
    @Help("The block that is placed.")
    val block: Material = Material.STONE,
) : EventEntry

@EntryListener(BlockPlaceEventEntry::class)
fun onPlaceBlock(event: BlockPlaceEvent, query: Query<BlockPlaceEventEntry>) {
    query findWhere { entry ->
        // Check if the player clicked on the correct location
        if (!entry.location.map { it == event.block.location }.orElse(true)) return@findWhere false

        entry.block == event.block.type
    } startDialogueWithOrNextDialogue event.player
}