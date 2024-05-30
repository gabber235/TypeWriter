package lirand.api.dsl.menu.builders.dynamic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lirand.api.dsl.menu.builders.fixed.StaticSlotDSLEventHandler
import lirand.api.dsl.menu.exposed.PlayerMenuSlotRenderEvent
import lirand.api.dsl.menu.exposed.dynamic.SlotEventHandler
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

typealias MenuPlayerSlotRenderCallback<I> = PlayerMenuSlotRenderEvent<I>.(scope: CoroutineScope) -> Unit

open class SlotDSLEventHandler<I : Inventory>(plugin: Plugin)
	: StaticSlotDSLEventHandler<I>(plugin), SlotEventHandler<I> {

	val renderCallbacks = mutableListOf<MenuPlayerSlotRenderCallback<I>>()

	override fun handleRender(renderEvent: PlayerMenuSlotRenderEvent<I>) {
		for (callback in renderCallbacks) {
			scope.launch {
				callback(renderEvent, this)
			}
		}
	}

	override fun clone(plugin: Plugin): SlotDSLEventHandler<I> {
		return SlotDSLEventHandler<I>(plugin).also {
			it.interactCallbacks.addAll(interactCallbacks)
			it.updateCallbacks.addAll(updateCallbacks)
			it.renderCallbacks.addAll(renderCallbacks)
		}
	}

}