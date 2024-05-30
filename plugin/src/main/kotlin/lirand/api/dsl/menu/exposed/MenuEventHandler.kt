package lirand.api.dsl.menu.exposed

import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin

interface MenuEventHandler<I : Inventory> {

	val plugin: Plugin

	fun handleUpdate(updateEvent: PlayerMenuUpdateEvent<I>)

	fun handleClose(closeEvent: PlayerMenuCloseEvent)

	fun handleMoveToMenu(moveToMenuEvent: PlayerMoveToMenuEvent<I>)

	fun handlePreOpen(preOpenEvent: PlayerMenuPreOpenEvent)

	fun handleOpen(openEvent: PlayerMenuOpenEvent<I>)

}