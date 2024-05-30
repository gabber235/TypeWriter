package lirand.api.dsl.menu.exposed.dynamic

import lirand.api.dsl.menu.exposed.fixed.StaticSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

interface Slot<I : Inventory> : StaticSlot<I> {

	override val eventHandler: SlotEventHandler<I>

	/**
	 * a clone of Slot without slotData and playerSlotData
	 */
	fun clone(item: ItemStack?, plugin: Plugin = this.plugin): Slot<I>

}