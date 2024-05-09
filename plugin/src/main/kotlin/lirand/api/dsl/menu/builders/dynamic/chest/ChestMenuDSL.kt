package lirand.api.dsl.menu.builders.dynamic.chest

import lirand.api.dsl.menu.builders.dynamic.MenuDSL
import lirand.api.dsl.menu.builders.dynamic.SlotDSL
import lirand.api.dsl.menu.exposed.calculateSlot
import lirand.api.dsl.menu.exposed.dynamic.Slot
import lirand.api.dsl.menu.exposed.dynamic.chest.ChestMenu
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

inline fun Plugin.chestMenu(
	lines: Int,
	cancelOnClick: Boolean = true,
	crossinline builder: ChestMenuDSL.() -> Unit = {}
): ChestMenu = ChestMenuImpl(this, lines, cancelOnClick).apply(builder)

inline fun ChestMenuDSL.slot(
	line: Int,
	slot: Int,
	item: ItemStack? = null,
	crossinline builder: SlotDSL<Inventory>.() -> Unit = {}
): Slot<Inventory> = slot(calculateSlot(line, slot), item, builder)

inline fun ChestMenuDSL.slot(
	slot: Int,
	item: ItemStack? = null,
	crossinline builder: SlotDSL<Inventory>.() -> Unit = {}
): Slot<Inventory> = (baseSlot.clone(item) as SlotDSL).apply(builder).also {
	setSlot(slot, it)
}

interface ChestMenuDSL : ChestMenu, MenuDSL<Slot<Inventory>, Inventory> {

	override var lines: Int

}