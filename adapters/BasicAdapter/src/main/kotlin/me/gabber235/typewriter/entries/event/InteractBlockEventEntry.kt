package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.BLOCK
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.ITEM
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

@Entry("on_interact_with_block", "When the player interacts with a block", Colors.YELLOW, Icons.HAND_POINTER)
class InteractBlockEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val location: Optional<Location> = Optional.empty(),
	@MaterialProperties(ITEM)
	val itemInHand: Optional<Material> = Optional.empty(),
	@MaterialProperties(BLOCK)
	val block: Material = Material.AIR,
) : EventEntry

private fun hasMaterialInHand(player: Player, material: Material): Boolean {
	return player.inventory.itemInMainHand.type == material || player.inventory.itemInOffHand.type == material
}

@EntryListener(InteractBlockEventEntry::class)
fun onInteractBlock(event: PlayerInteractEvent, query: Query<InteractBlockEventEntry>) {
	if (event.clickedBlock == null) return
	if (event.action != Action.RIGHT_CLICK_BLOCK) return
	// The even triggers twice. Both for the main hand and offhand.
	// We only want to trigger once.
	if (event.hand != org.bukkit.inventory.EquipmentSlot.HAND) return // Disable off-hand interactions
	query findWhere { entry ->
		// Check if the player clicked on the correct location
		if (!entry.location.map { it == event.clickedBlock!!.location }.orElse(true)) return@findWhere false

		// Check if the player is holding the correct item
		if (!entry.itemInHand.map { hasMaterialInHand(event.player, it) }.orElse(true)) return@findWhere false

		entry.block == event.clickedBlock!!.type
	} triggerAllFor event.player
}