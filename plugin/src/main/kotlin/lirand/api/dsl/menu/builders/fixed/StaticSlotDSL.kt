package lirand.api.dsl.menu.builders.fixed

import lirand.api.dsl.menu.exposed.fixed.StaticSlot
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

@MenuDSLMarker
interface StaticSlotDSL<I : Inventory> : StaticSlot<I> {

	var cancelEvents: Boolean

	override val eventHandler: StaticSlotDSLEventHandler<I>


	fun onInteract(interactCallback: MenuPlayerSlotInteractCallback<I>) {
		eventHandler.interactCallbacks.add(interactCallback)
	}

	fun onUpdate(updateCallback: MenuPlayerSlotUpdateCallback<I>) {
		eventHandler.updateCallbacks.add(updateCallback)
	}

	override fun clone(plugin: Plugin): StaticSlotDSL<I>

}