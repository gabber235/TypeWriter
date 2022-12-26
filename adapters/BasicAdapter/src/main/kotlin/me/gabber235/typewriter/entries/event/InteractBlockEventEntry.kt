package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.BLOCK
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.ITEM
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Material
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

@Entry("on_interact_with_block", "When the player interacts with a block", Colors.YELLOW, Icons.HAND_POINTER)
class InteractBlockEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@MaterialProperties(ITEM)
	val itemInHand: Material = Material.AIR,
	@MaterialProperties(BLOCK)
	val block: Material = Material.AIR,
) : EventEntry

@EntryListener(InteractBlockEventEntry::class)
fun onInteractBlock(event: PlayerInteractEvent, query: Query<InteractBlockEventEntry>) {
	if (event.clickedBlock == null) return
	if (event.action != Action.RIGHT_CLICK_BLOCK) return
	// The even triggers twice. Both for the main hand and offhand.
	// We only want to trigger once.
	if (event.hand != org.bukkit.inventory.EquipmentSlot.HAND) return // Disable off-hand interactions
	query findWhere {
		(it.itemInHand == Material.AIR || it.itemInHand == event.player.inventory.itemInMainHand.type || it.itemInHand == event.player.inventory.itemInOffHand.type) &&
				(it.block == Material.AIR || it.block == event.clickedBlock?.type)
	} triggerAllFor event.player
}