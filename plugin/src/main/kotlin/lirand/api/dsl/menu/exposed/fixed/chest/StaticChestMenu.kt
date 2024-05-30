package lirand.api.dsl.menu.exposed.fixed.chest

import lirand.api.dsl.menu.exposed.fixed.StaticMenu
import lirand.api.dsl.menu.exposed.fixed.StaticSlot
import org.bukkit.inventory.Inventory

interface StaticChestMenu : StaticMenu<StaticSlot<Inventory>, Inventory> {

	val lines: Int

	fun setInventory(inventory: Inventory)

}