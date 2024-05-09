package lirand.api.dsl.menu.exposed.dynamic.chest.pagination

import lirand.api.dsl.menu.exposed.PlayerInventoryMenuEvent
import lirand.api.dsl.menu.exposed.PlayerMenuEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

interface PaginationMenuEventHandler {

	val plugin: Plugin

	fun handlePageChange(pageChangeEvent: PlayerInventoryMenuEvent<Inventory>)

	fun handlePageAvailable(pageAvailableEvent: PlayerMenuEvent)

}