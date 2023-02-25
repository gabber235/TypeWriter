package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.ITEM
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import java.util.*

@Entry("on_item_pickup", "When the player picks up an item", Colors.YELLOW, Icons.HAND_SPARKLES)
class PickupItemEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@MaterialProperties(ITEM)
	@Help("The item that was picked up.")
	val material: Material = Material.STONE,
) : EventEntry

@EntryListener(PickupItemEventEntry::class)
fun onPickupItem(event: EntityPickupItemEvent, query: Query<PickupItemEventEntry>) {
	if (event.entity !is Player) return

	val player = event.entity as Player

	query findWhere { entry ->
		entry.material == event.item.itemStack.type
	} startDialogueWithOrNextDialogue player
}