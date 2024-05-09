package lirand.api.dsl.menu.builders.dynamic.chest.pagination

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lirand.api.dsl.menu.exposed.PlayerInventoryMenuEvent
import lirand.api.dsl.menu.exposed.PlayerMenuEvent
import lirand.api.dsl.menu.exposed.dynamic.chest.pagination.PaginationMenuEventHandler
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

typealias MenuPlayerPageChangeCallback = PlayerInventoryMenuEvent<Inventory>.(scope: CoroutineScope) -> Unit
typealias MenuPlayerPageAvailableCallback = PlayerMenuEvent.(scope: CoroutineScope) -> Unit

class PaginationDSLEventHandler(override val plugin: Plugin) : PaginationMenuEventHandler {

	private val scope = CoroutineScope(
		plugin.minecraftDispatcher + SupervisorJob() +
				CoroutineExceptionHandler { _, exception -> exception.printStackTrace() }
	)

	val pageChangeCallbacks = mutableListOf<MenuPlayerPageChangeCallback>()
	val pageAvailableCallbacks = mutableListOf<MenuPlayerPageAvailableCallback>()

	override fun handlePageChange(pageChangeEvent: PlayerInventoryMenuEvent<Inventory>) {
		for (callback in pageChangeCallbacks) {
			scope.launch {
				callback(pageChangeEvent, this)
			}
		}
	}

	override fun handlePageAvailable(pageAvailableEvent: PlayerMenuEvent) {
		for (callback in pageAvailableCallbacks) {
			scope.launch {
				callback(pageAvailableEvent, this)
			}
		}
	}

}