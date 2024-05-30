package lirand.api.dsl.menu.exposed.fixed

import lirand.api.dsl.menu.exposed.PlayerMenuSlotInteractEvent
import lirand.api.dsl.menu.exposed.PlayerMenuSlotUpdateEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

interface StaticSlotEventHandler<I : Inventory> {

	val plugin: Plugin

	fun handleInteract(interactEvent: PlayerMenuSlotInteractEvent<I>)

	fun handleUpdate(updateEvent: PlayerMenuSlotUpdateEvent<I>)

	fun clone(plugin: Plugin = this.plugin): StaticSlotEventHandler<I>

}