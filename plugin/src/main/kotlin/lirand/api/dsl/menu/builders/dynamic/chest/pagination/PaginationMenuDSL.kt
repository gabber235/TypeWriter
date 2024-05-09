package lirand.api.dsl.menu.builders.dynamic.chest.pagination

import lirand.api.dsl.menu.builders.dynamic.chest.ChestMenuDSL
import lirand.api.dsl.menu.builders.dynamic.chest.pagination.slot.PaginationSlotDSL
import lirand.api.dsl.menu.builders.fixed.MenuDSLMarker
import lirand.api.dsl.menu.exposed.dynamic.Slot
import lirand.api.dsl.menu.exposed.dynamic.chest.pagination.ItemsAdapter
import lirand.api.dsl.menu.exposed.dynamic.chest.pagination.ItemsProvider
import lirand.api.dsl.menu.exposed.dynamic.chest.pagination.PaginationMenu
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

inline fun <T> ChestMenuDSL.pagination(
	noinline itemsProvider: ItemsProvider<T>,
	previousPageSlot: Slot<Inventory>? = null,
	nextPageSlot: Slot<Inventory>? = null,
	linesRange: IntRange = 1 until lines,
	slotsRange: IntRange = 1..9,
	autoUpdateSwitchPageSlot: Boolean = true,
	crossinline builder: PaginationMenuDSL<T>.() -> Unit
): PaginationMenu<T> =
	PaginationMenuImpl(
		this,
		itemsProvider,
		previousPageSlot, nextPageSlot,
		linesRange, slotsRange,
		autoUpdateSwitchPageSlot
	).apply(builder)


inline fun <T> PaginationMenuDSL<T>.slot(
	crossinline builder: PaginationSlotDSL<T>.() -> Unit
) {
	for (slot in slots.values) {
		(slot as PaginationSlotDSL<T>).builder()
	}
}

fun ChestMenuDSL.setPlayerOpenPage(player: Player, page: Int) {
	playerData[player][PAGINATION_OPEN_PAGE_KEY] = page
}


@MenuDSLMarker
interface PaginationMenuDSL<T> : PaginationMenu<T> {

	override val menu: ChestMenuDSL
	override val eventHandler: PaginationDSLEventHandler

	override val previousPageSlot: Slot<Inventory>?
	override val nextPageSlot: Slot<Inventory>?

	fun onPageChange(pageChangeCallback: MenuPlayerPageChangeCallback) {
		eventHandler.pageChangeCallbacks.add(pageChangeCallback)
	}

	fun onPageAvailable(pageAvailableCallback: MenuPlayerPageAvailableCallback) {
		eventHandler.pageAvailableCallbacks.add(pageAvailableCallback)
	}

	fun adaptOnUpdate(adapter: ItemsAdapter<T>)

}