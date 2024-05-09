package lirand.api.dsl.menu.exposed.dynamic.chest.pagination.slot

import lirand.api.dsl.menu.exposed.dynamic.Slot
import lirand.api.dsl.menu.exposed.MenuPlayerDataMap
import lirand.api.dsl.menu.exposed.MenuTypedDataMap
import org.bukkit.inventory.Inventory

interface PaginationSlot<T> {

	val slotRoot: Slot<Inventory>

	val eventHandler: PaginationSlotEventHandler<T>

	val slotData: MenuTypedDataMap
	val playerSlotData: MenuPlayerDataMap

}