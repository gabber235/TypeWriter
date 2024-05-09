package lirand.api.dsl.menu.builders.fixed

import lirand.api.dsl.menu.builders.MenuDSLEventHandler
import lirand.api.dsl.menu.builders.PlayerMenuCloseCallback
import lirand.api.dsl.menu.builders.PlayerMenuMoveCallback
import lirand.api.dsl.menu.builders.PlayerMenuOpenCallback
import lirand.api.dsl.menu.builders.PlayerMenuPreOpenCallback
import lirand.api.dsl.menu.builders.PlayerMenuUpdateCallback
import lirand.api.dsl.menu.exposed.fixed.StaticMenu
import lirand.api.dsl.menu.exposed.fixed.StaticSlot
import org.bukkit.inventory.Inventory

@DslMarker
@Retention(AnnotationRetention.BINARY)
annotation class MenuDSLMarker

@MenuDSLMarker
interface StaticMenuDSL<S : StaticSlot<I>, I : Inventory> : StaticMenu<S, I> {

	var cancelEvents: Boolean

	override val eventHandler: MenuDSLEventHandler<I>


	fun onUpdate(updateCallback: PlayerMenuUpdateCallback<I>) {
		eventHandler.updateCallbacks.add(updateCallback)
	}

	fun onClose(closeCallback: PlayerMenuCloseCallback) {
		eventHandler.closeCallbacks.add(closeCallback)
	}

	fun onMoveToMenu(moveToMenuCallback: PlayerMenuMoveCallback<I>) {
		eventHandler.moveToMenuCallbacks.add(moveToMenuCallback)
	}

	fun preOpen(preOpenCallback: PlayerMenuPreOpenCallback) {
		eventHandler.preOpenCallbacks.add(preOpenCallback)
	}

	fun onOpen(openCallback: PlayerMenuOpenCallback<I>) {
		eventHandler.openCallbacks.add(openCallback)
	}

}