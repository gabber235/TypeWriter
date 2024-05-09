package lirand.api.dsl.menu.builders.dynamic

import lirand.api.dsl.menu.builders.fixed.StaticMenuDSL
import lirand.api.dsl.menu.exposed.PlayerMenuEvent
import lirand.api.dsl.menu.exposed.dynamic.Menu
import lirand.api.dsl.menu.exposed.dynamic.Slot
import org.bukkit.inventory.Inventory

interface MenuDSL<S : Slot<I>, I : Inventory> : Menu<S, I>, StaticMenuDSL<S, I> {

	override var title: String?

	fun title(render: PlayerMenuEvent.() -> String?)

}