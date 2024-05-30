package lirand.api.dsl.menu.builders.fixed

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import lirand.api.dsl.menu.exposed.PlayerMenuSlotInteractEvent
import lirand.api.dsl.menu.exposed.PlayerMenuSlotUpdateEvent
import lirand.api.dsl.menu.exposed.fixed.StaticSlotEventHandler
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

typealias MenuPlayerSlotUpdateCallback<I> = PlayerMenuSlotUpdateEvent<I>.(scope: CoroutineScope) -> Unit
typealias MenuPlayerSlotInteractCallback<I> = PlayerMenuSlotInteractEvent<I>.(scope: CoroutineScope) -> Unit

open class StaticSlotDSLEventHandler<I : Inventory>(override val plugin: Plugin) : StaticSlotEventHandler<I> {

	protected val scope = CoroutineScope(
		plugin.minecraftDispatcher + SupervisorJob() +
				CoroutineExceptionHandler { _, exception -> exception.printStackTrace() }
	)

	val interactCallbacks = mutableListOf<MenuPlayerSlotInteractCallback<I>>()
	val updateCallbacks = mutableListOf<MenuPlayerSlotUpdateCallback<I>>()

	override fun handleInteract(interactEvent: PlayerMenuSlotInteractEvent<I>) {
		for (callback in interactCallbacks) {
			scope.launch {
				callback(interactEvent, this)
			}
		}
	}

	override fun handleUpdate(updateEvent: PlayerMenuSlotUpdateEvent<I>) {
		for (callback in updateCallbacks) {
			scope.launch {
				callback(updateEvent, this)
			}
		}
	}

	override fun clone(plugin: Plugin): StaticSlotDSLEventHandler<I> {
		return StaticSlotDSLEventHandler<I>(plugin).also {
			it.interactCallbacks.addAll(interactCallbacks)
			it.updateCallbacks.addAll(updateCallbacks)
		}
	}

}