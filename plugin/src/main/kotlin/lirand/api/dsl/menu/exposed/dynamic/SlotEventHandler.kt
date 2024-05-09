package lirand.api.dsl.menu.exposed.dynamic

import lirand.api.dsl.menu.exposed.PlayerMenuSlotRenderEvent
import lirand.api.dsl.menu.exposed.fixed.StaticSlotEventHandler
import org.bukkit.inventory.Inventory

interface SlotEventHandler<I : Inventory> : StaticSlotEventHandler<I> {

	fun handleRender(renderEvent: PlayerMenuSlotRenderEvent<I>)

}