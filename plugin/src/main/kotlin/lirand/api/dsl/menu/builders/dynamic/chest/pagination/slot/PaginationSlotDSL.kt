package lirand.api.dsl.menu.builders.dynamic.chest.pagination.slot

import lirand.api.dsl.menu.builders.dynamic.SlotDSL
import lirand.api.dsl.menu.builders.fixed.MenuDSLMarker
import lirand.api.dsl.menu.exposed.dynamic.chest.pagination.slot.PaginationSlot
import org.bukkit.inventory.Inventory

@MenuDSLMarker
interface PaginationSlotDSL<T> : PaginationSlot<T> {

	override val slotRoot: SlotDSL<Inventory>

	override val eventHandler: PaginationSlotDSLEventHandler<T>

	/**
	 * Cancel the interaction with this slot
	 */
	var cancelEvents: Boolean

	fun onPageChange(pageChangeCallback: MenuSlotPageChangeCallback<T>) {
		eventHandler.pageChangeCallbacks.add(pageChangeCallback)
	}

	fun onInteract(clickCallback: MenuPageSlotInteractCallback<T>) {
		eventHandler.interactCallbacks.add(clickCallback)
	}

	fun onRender(renderCallback: MenuPageSlotRenderCallback<T>) {
		eventHandler.renderCallbacks.add(renderCallback)
	}

	fun onUpdate(updateCallback: MenuPageSlotUpdateCallback<T>) {
		eventHandler.updateCallbacks.add(updateCallback)
	}

}

