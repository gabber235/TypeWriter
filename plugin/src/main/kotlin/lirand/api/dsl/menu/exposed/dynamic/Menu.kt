package lirand.api.dsl.menu.exposed.dynamic

import lirand.api.dsl.menu.exposed.fixed.StaticMenu
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

interface Menu<S : Slot<I>, I : Inventory> : StaticMenu<S, I> {

	fun update(player: Player)
	fun updateSlot(slot: S, player: Player)
	fun rerenderSlot(slot: S, player: Player)

}