package lirand.api.dsl.menu.builders.dynamic.chest.slot

import lirand.api.dsl.menu.builders.dynamic.SlotDSL
import lirand.api.dsl.menu.builders.dynamic.SlotDSLEventHandler
import lirand.api.dsl.menu.exposed.MenuPlayerDataMap
import lirand.api.dsl.menu.exposed.MenuTypedDataMap
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin


class ChestSlot(
	override val plugin: Plugin,
	override val item: ItemStack?,
	override var cancelEvents: Boolean,
	override val eventHandler: SlotDSLEventHandler<Inventory>
) : SlotDSL<Inventory> {

	override val slotData = MenuTypedDataMap()
	override val playerSlotData = MenuPlayerDataMap()


	override fun clone(item: ItemStack?, plugin: Plugin) =
		ChestSlot(plugin, item, cancelEvents, eventHandler.clone(plugin))

	override fun clone(plugin: Plugin) = clone(item, plugin)

}