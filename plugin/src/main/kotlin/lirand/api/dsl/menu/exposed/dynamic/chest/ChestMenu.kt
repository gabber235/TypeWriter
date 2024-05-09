package lirand.api.dsl.menu.exposed.dynamic.chest

import lirand.api.dsl.menu.exposed.dynamic.Menu
import lirand.api.dsl.menu.exposed.dynamic.Slot
import org.bukkit.inventory.Inventory

interface ChestMenu : Menu<Slot<Inventory>, Inventory> {

	val lines: Int

}