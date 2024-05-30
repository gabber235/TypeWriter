package lirand.api.dsl.menu.builders.dynamic.chest.pagination.slot

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lirand.api.dsl.menu.exposed.PlayerMenuSlotInteractEvent
import lirand.api.dsl.menu.exposed.PlayerMenuSlotRenderEvent
import lirand.api.dsl.menu.exposed.PlayerMenuSlotPageChangeEvent
import lirand.api.dsl.menu.exposed.PlayerMenuSlotUpdateEvent
import lirand.api.dsl.menu.exposed.dynamic.chest.pagination.slot.PaginationSlotEventHandler
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

typealias MenuSlotPageChangeCallback<T> =
		PlayerMenuSlotPageChangeEvent.(provided: T?, scope: CoroutineScope) -> Unit
typealias MenuPageSlotInteractCallback<T> =
		PlayerMenuSlotInteractEvent<Inventory>.(provided: T?, scope: CoroutineScope) -> Unit
typealias MenuPageSlotRenderCallback<T> =
		PlayerMenuSlotRenderEvent<Inventory>.(provided: T?, scope: CoroutineScope) -> Unit
typealias MenuPageSlotUpdateCallback<T> =
		PlayerMenuSlotUpdateEvent<Inventory>.(provided: T?, scope: CoroutineScope) -> Unit


class PaginationSlotDSLEventHandler<T>(override val plugin: Plugin) : PaginationSlotEventHandler<T> {

	private val scope = CoroutineScope(
		plugin.minecraftDispatcher + SupervisorJob() +
				CoroutineExceptionHandler { _, exception -> exception.printStackTrace() }
	)

	val pageChangeCallbacks = mutableListOf<MenuSlotPageChangeCallback<T>>()
	val interactCallbacks = mutableListOf<MenuPageSlotInteractCallback<T>>()
	val renderCallbacks = mutableListOf<MenuPageSlotRenderCallback<T>>()
	val updateCallbacks = mutableListOf<MenuPageSlotUpdateCallback<T>>()

	override fun handlePageChange(provided: T?, pageChangeEvent: PlayerMenuSlotPageChangeEvent) {
		for (callback in pageChangeCallbacks) {
			scope.launch {
				callback(pageChangeEvent, provided, this)
			}
		}
	}

	override fun handleRender(provided: T?, renderEvent: PlayerMenuSlotRenderEvent<Inventory>) {
		for (callback in renderCallbacks) {
			scope.launch {
				callback(renderEvent, provided, this)
			}
		}
	}

	override fun handleUpdate(provided: T?, updateEvent: PlayerMenuSlotUpdateEvent<Inventory>) {
		for (callback in updateCallbacks) {
			scope.launch {
				callback(updateEvent, provided, this)
			}
		}
	}

	override fun handleInteract(provided: T?, interactEvent: PlayerMenuSlotInteractEvent<Inventory>) {
		for (callback in interactCallbacks) {
			scope.launch {
				callback(interactEvent, provided, this)
			}
		}
	}

}