package lirand.api.dsl.menu.exposed.fixed

import lirand.api.dsl.menu.exposed.*
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.plugin.Plugin
import kotlin.time.Duration

interface StaticMenu<S : StaticSlot<I>, I : Inventory> : InventoryHolder {

	val plugin: Plugin
	val title: String?

	val rangeOfSlots: IntRange
	val slots: Map<Int, S>

	var baseSlot: S
	var updateDelay: Duration

	val views: Map<Player, MenuView<I>>

	val data: MenuTypedDataMap
	val playerData: MenuPlayerDataMap

	val eventHandler: MenuEventHandler<I>

	fun setSlot(index: Int, slot: S)
	fun removeSlot(index: Int)
	fun clearSlots()

	fun update()
	fun updateSlot(slot: S)


	fun open(player: Player, backStack: MenuBackStack? = null)

	fun close(player: Player, closeInventory: Boolean = true)

	fun back(player: Player, key: String? = null)


	fun clearData() {
		data.clear()
		for (slot in getSlotsWithBaseSlot())
			slot.slotData.clear()
	}

	fun clearPlayerData(player: Player) {
		playerData.remove(player)
		for (slot in getSlotsWithBaseSlot())
			slot.playerSlotData.remove(player)
	}
}