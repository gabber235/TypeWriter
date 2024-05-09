package lirand.api.dsl.menu.builders.fixed.chest

import lirand.api.dsl.menu.builders.dynamic.SlotDSLEventHandler
import lirand.api.dsl.menu.builders.dynamic.chest.slot.ChestSlot
import lirand.api.dsl.menu.builders.fixed.StaticMenuDSL
import lirand.api.dsl.menu.builders.fixed.StaticSlotDSL
import lirand.api.dsl.menu.exposed.calculateSlot
import lirand.api.dsl.menu.exposed.fixed.StaticSlot
import lirand.api.dsl.menu.exposed.fixed.chest.StaticChestMenu
import lirand.api.extensions.inventory.set
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

inline fun Plugin.staticChestMenu(
	lines: Int,
	title: String,
	cancelEvents: Boolean = false,
	crossinline builder: StaticChestMenuDSL.() -> Unit = {}
): StaticChestMenuDSL = StaticChestMenuImpl(this, lines, title, cancelEvents).apply(builder)

inline fun Plugin.staticChestMenu(
	inventory: Inventory,
	title: String,
	cancelEvents: Boolean = false,
	crossinline builder: StaticChestMenuDSL.() -> Unit = {}
): StaticChestMenuDSL {
	require(inventory.type == InventoryType.CHEST) { "Illegal inventory type." }

	return StaticChestMenuImpl(this, inventory.size / 9, title, cancelEvents).apply {
		this.inventory = inventory
		builder()
	}
}


inline fun StaticChestMenuDSL.slot(
	line: Int,
	slot: Int,
	crossinline builder: StaticSlotDSL<Inventory>.() -> Unit = {}
): StaticSlot<Inventory> = slot(calculateSlot(line, slot), builder)

inline fun StaticChestMenuDSL.slot(
	line: Int,
	slot: Int,
	item: ItemStack?,
	crossinline builder: StaticSlotDSL<Inventory>.() -> Unit = {}
): StaticSlot<Inventory> = slot(calculateSlot(line, slot), item, builder)

inline fun StaticChestMenuDSL.slot(
	slot: Int,
	crossinline builder: StaticSlotDSL<Inventory>.() -> Unit = {}
): StaticSlot<Inventory> = (baseSlot.clone() as StaticSlotDSL<Inventory>).apply(builder).also {
	setSlot(slot, it)
}

inline fun StaticChestMenuDSL.slot(
	slot: Int,
	item: ItemStack?,
	crossinline builder: StaticSlotDSL<Inventory>.() -> Unit = {}
): StaticSlotDSL<Inventory> =
	ChestSlot(plugin, item, cancelEvents, SlotDSLEventHandler(plugin)).apply(builder).also {
		inventory[slot] = item
		setSlot(slot, it)
	}


interface StaticChestMenuDSL : StaticChestMenu, StaticMenuDSL<StaticSlot<Inventory>, Inventory>