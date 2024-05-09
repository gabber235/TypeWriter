package lirand.api.dsl.menu.builders

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lirand.api.dsl.menu.exposed.MenuEventHandler
import lirand.api.dsl.menu.exposed.PlayerMenuCloseEvent
import lirand.api.dsl.menu.exposed.PlayerMenuOpenEvent
import lirand.api.dsl.menu.exposed.PlayerMenuPreOpenEvent
import lirand.api.dsl.menu.exposed.PlayerMenuUpdateEvent
import lirand.api.dsl.menu.exposed.PlayerMoveToMenuEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

typealias PlayerMenuUpdateCallback<I> = PlayerMenuUpdateEvent<I>.(scope: CoroutineScope) -> Unit
typealias PlayerMenuCloseCallback = PlayerMenuCloseEvent.(scope: CoroutineScope) -> Unit
typealias PlayerMenuMoveCallback<I> = PlayerMoveToMenuEvent<I>.(scope: CoroutineScope) -> Unit

typealias PlayerMenuPreOpenCallback = PlayerMenuPreOpenEvent.(scope: CoroutineScope) -> Unit
typealias PlayerMenuOpenCallback<I> = PlayerMenuOpenEvent<I>.(scope: CoroutineScope) -> Unit

open class MenuDSLEventHandler<I : Inventory>(final override val plugin: Plugin) : MenuEventHandler<I> {

	protected val scope = CoroutineScope(
		plugin.minecraftDispatcher + SupervisorJob() +
				CoroutineExceptionHandler { _, exception -> exception.printStackTrace() }
	)

	val updateCallbacks = mutableListOf<PlayerMenuUpdateCallback<I>>()
	val closeCallbacks = mutableListOf<PlayerMenuCloseCallback>()
	val moveToMenuCallbacks = mutableListOf<PlayerMenuMoveCallback<I>>()
	val preOpenCallbacks = mutableListOf<PlayerMenuPreOpenCallback>()
	val openCallbacks = mutableListOf<PlayerMenuOpenCallback<I>>()

	override fun handleUpdate(updateEvent: PlayerMenuUpdateEvent<I>) {
		for (callback in updateCallbacks) {
			scope.launch {
				callback(updateEvent, this)
			}
		}
	}

	override fun handleClose(closeEvent: PlayerMenuCloseEvent) {
		for (callback in closeCallbacks) {
			scope.launch {
				callback(closeEvent, this)
			}
		}
	}

	override fun handleMoveToMenu(moveToMenuEvent: PlayerMoveToMenuEvent<I>) {
		for (callback in moveToMenuCallbacks) {
			scope.launch {
				callback(moveToMenuEvent, this)
			}
		}
	}

	override fun handlePreOpen(preOpenEvent: PlayerMenuPreOpenEvent) {
		for (callback in preOpenCallbacks) {
			scope.launch {
				callback(preOpenEvent, this)
			}
		}
	}

	override fun handleOpen(openEvent: PlayerMenuOpenEvent<I>) {
		for (callback in openCallbacks) {
			scope.launch {
				callback(openEvent, this)
			}
		}
	}

}