package lirand.api.dsl.menu.builders.dynamic

import lirand.api.dsl.menu.builders.fixed.StaticSlotDSL
import lirand.api.dsl.menu.exposed.dynamic.Slot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

interface SlotDSL<I : Inventory> : Slot<I>, StaticSlotDSL<I> {

	override val eventHandler: SlotDSLEventHandler<I>

	fun onRender(renderCallback: MenuPlayerSlotRenderCallback<I>) {
		eventHandler.renderCallbacks.add(renderCallback)
	}

	override fun clone(item: ItemStack?, plugin: Plugin): SlotDSL<I>
	override fun clone(plugin: Plugin): SlotDSL<I>

}