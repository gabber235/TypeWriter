package lirand.api.dsl.menu.exposed.dynamic.chest.pagination

import lirand.api.dsl.menu.exposed.PlayerInventoryMenuEvent
import lirand.api.dsl.menu.exposed.PlayerMenuEvent
import lirand.api.dsl.menu.exposed.dynamic.Slot
import lirand.api.dsl.menu.exposed.dynamic.chest.ChestMenu
import lirand.api.dsl.menu.exposed.dynamic.chest.pagination.slot.PaginationSlot
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

typealias ItemsProvider<T> = PlayerMenuEvent.() -> Collection<T>
typealias ItemsAdapter<T> = PlayerInventoryMenuEvent<Inventory>.(Collection<T>) -> Collection<T>


interface PaginationMenu<T> {

	val menu: ChestMenu

	val eventHandler: PaginationMenuEventHandler

	val itemsProvider: ItemsProvider<T>
	val itemsAdapter: ItemsAdapter<T>?

	val autoUpdateSwitchPageSlots: Boolean

	val slots: Map<Int, PaginationSlot<T>>
	val nextPageSlot: Slot<Inventory>?
	val previousPageSlot: Slot<Inventory>?

	val linesRange: IntRange
	val slotsRange: IntRange

	fun hasPreviousPage(player: Player): Boolean
	fun hasNextPage(player: Player): Boolean
	fun getPlayerCurrentPage(player: Player): Int

	fun updateItems(player: Player)

}