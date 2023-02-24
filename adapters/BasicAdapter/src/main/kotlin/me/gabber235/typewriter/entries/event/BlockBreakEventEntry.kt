package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import java.util.*

@Entry("on_block_break", "When the player breaks a block", Colors.YELLOW, Icons.HAND_POINTER)
class BlockBreakEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Help("The location of the block that was broken.")
	val location: Optional<Location> = Optional.empty(),
	@MaterialProperties(MaterialProperty.ITEM)
	@Help("The item the player is holding when the block is broken.")
	val itemInHand: Optional<Material> = Optional.empty(),
	@MaterialProperties(MaterialProperty.BLOCK)
	@Help("The block that was broken.")
	val block: Optional<Material> = Optional.empty(),
) : EventEntry

private fun hasMaterialInHand(player: Player, material: Material): Boolean {
	return player.inventory.itemInMainHand.type == material || player.inventory.itemInOffHand.type == material
}

@EntryListener(BlockBreakEventEntry::class)
fun onBlockBreak(event: BlockBreakEvent, query: Query<BlockBreakEventEntry>) {
	query findWhere { entry ->
		// Check if the player clicked on the correct location
		if (!entry.location.map { it == event.block.location }.orElse(true)) return@findWhere false

		// Check if the player is holding the correct item
		if (!entry.itemInHand.map { hasMaterialInHand(event.player, it) }.orElse(true)) return@findWhere false

		// Check if block type is correct
		entry.block.map { it == event.block.type }.orElse(true)
	} startDialogueWithOrNextDialogue event.player
}