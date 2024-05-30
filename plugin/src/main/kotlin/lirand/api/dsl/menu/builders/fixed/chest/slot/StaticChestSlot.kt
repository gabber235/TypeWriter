package lirand.api.dsl.menu.builders.fixed.chest.slot

import lirand.api.dsl.menu.builders.fixed.StaticSlotDSL
import lirand.api.dsl.menu.builders.fixed.StaticSlotDSLEventHandler
import lirand.api.dsl.menu.builders.fixed.chest.StaticChestMenuDSL
import lirand.api.dsl.menu.exposed.MenuPlayerDataMap
import lirand.api.dsl.menu.exposed.MenuTypedDataMap
import lirand.api.extensions.inventory.get
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class StaticChestSlot(
	override val plugin: Plugin,
	override var cancelEvents: Boolean,
	override val eventHandler: StaticSlotDSLEventHandler<Inventory>,
) : StaticSlotDSL<Inventory> {

	internal var menu: StaticChestMenuDSL? = null
	internal var slotIndex = 0

	override val item: ItemStack?
		get() = menu?.inventory?.get(slotIndex - 1)

	override val slotData = MenuTypedDataMap()
	override val playerSlotData = MenuPlayerDataMap()

	override fun clone(plugin: Plugin) = StaticChestSlot(plugin, cancelEvents, eventHandler.clone(plugin))

}