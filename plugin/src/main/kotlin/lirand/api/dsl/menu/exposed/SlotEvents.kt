package lirand.api.dsl.menu.exposed

import lirand.api.dsl.menu.exposed.dynamic.chest.ChestMenu
import lirand.api.dsl.menu.exposed.fixed.StaticMenu
import lirand.api.dsl.menu.exposed.fixed.StaticSlot
import lirand.api.extensions.inventory.get
import lirand.api.extensions.inventory.set
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface PlayerMenuSlotEvent : PlayerMenuEvent {
	val slotIndex: Int
	val slot: StaticSlot<*>

	val currentPlayerSlotData: MenuTypedDataMap
		get() = slot.playerSlotData[player]
}

interface PlayerMenuInventorySlotEvent<I : Inventory> : PlayerMenuSlotEvent, PlayerInventoryMenuEvent<I> {

	var currentItem: ItemStack?
		get() = inventory[slotIndex]?.takeIf { it.type != Material.AIR }
		set(value) {
			inventory[slotIndex] = value
		}

}



open class PlayerMenuSlotInteractEvent<I : Inventory>(
	menu: StaticMenu<*, *>,
	inventory: I,
	player: Player,
	override val slotIndex: Int,
	override val slot: StaticSlot<I>,
	isCanceled: Boolean,
	val click: ClickType,
	val action: InventoryAction,
	val clicked: ItemStack?,
	val cursor: ItemStack?,
	val hotbarKey: Int
) : PlayerMenuInteractEvent<I>(menu, player, inventory, isCanceled), PlayerMenuInventorySlotEvent<I>


class PlayerMenuSlotPageChangeEvent(
	override val menu: ChestMenu,
	override val slotIndex: Int,
	override val slot: StaticSlot<Inventory>,
	override val player: Player,
	override val inventory: Inventory
) : PlayerMenuInventorySlotEvent<Inventory>


class PlayerMenuSlotRenderEvent<I : Inventory>(
	override val menu: StaticMenu<*, *>,
	override val slotIndex: Int,
	override val slot: StaticSlot<I>,
	override val player: Player,
	override val inventory: I
) : PlayerMenuInventorySlotEvent<I>


class PlayerMenuSlotUpdateEvent<I : Inventory>(
	override val menu: StaticMenu<*, *>,
	override val slotIndex: Int,
	override val slot: StaticSlot<I>,
	override val player: Player,
	override val inventory: I
) : PlayerMenuInventorySlotEvent<I>