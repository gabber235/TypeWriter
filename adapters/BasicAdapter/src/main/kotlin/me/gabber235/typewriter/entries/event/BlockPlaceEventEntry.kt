package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.BLOCK
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.ITEM
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.entries.SystemTrigger
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

@Entry("on_place_block", "When the player places a block", Colors.YELLOW, Icons.CUBES_STACKED)
class BlockPlaceEventEntry (
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val location: Optional<Location> = Optional.empty(),
	@MaterialProperties(ITEM)
	val block: Material = Material.STONE,
) : EventEntry

@EntryListener(BlockPlaceEventEntry::class)
fun onPlaceBlock(event: BlockPlaceEvent, query: Query<BlockPlaceEventEntry>) {

	query.findWhere { entry ->
		// Check if the player clicked on the correct location
		if (!entry.location.map { it == event.block.location }.orElse(true)) return@findWhere false

		entry.block == event.block.type
	}.startInteractionWithOrTrigger(event.player, SystemTrigger.DIALOGUE_NEXT)
}