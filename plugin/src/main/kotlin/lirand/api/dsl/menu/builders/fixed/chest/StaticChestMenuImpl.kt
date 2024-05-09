package lirand.api.dsl.menu.builders.fixed.chest

import com.github.shynixn.mccoroutine.bukkit.ticks
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lirand.api.dsl.menu.builders.dynamic.chest.slot.ChestSlot
import lirand.api.dsl.menu.builders.fixed.AbstractStaticMenuDSL
import lirand.api.dsl.menu.builders.fixed.StaticSlotDSLEventHandler
import lirand.api.dsl.menu.builders.fixed.chest.slot.StaticChestSlot
import lirand.api.dsl.menu.exposed.*
import lirand.api.dsl.menu.exposed.fixed.*
import lirand.api.extensions.inventory.Inventory
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.time.Duration

class StaticChestMenuImpl(
	plugin: Plugin,
	override val lines: Int,
	override val title: String,
	cancelEvents: Boolean
) : AbstractStaticMenuDSL<StaticSlot<Inventory>, Inventory>(plugin, cancelEvents), StaticChestMenuDSL {

	private var _inventory: Inventory = Inventory(lines * 9, this, title)

	override val rangeOfSlots: IntRange = 0 until lines * 9

	override var baseSlot: StaticSlot<Inventory> = StaticChestSlot(plugin, cancelEvents, StaticSlotDSLEventHandler(plugin))


	override fun setSlot(index: Int, slot: StaticSlot<Inventory>) {
		if (index !in rangeOfSlots) return

		if (slot is StaticChestSlot && slot.menu == null && slot.slotIndex <= 0) {
			slot.menu = this
			slot.slotIndex = index
		}

		_slots[index] = slot
	}

	override fun removeSlot(index: Int) {
		_slots.remove(index)?.let {
			if (it is ChestSlot) {
				inventory.clear(index)
			}
		}
	}

	override fun clearSlots() {
		for ((index, slot) in slots) {
			_slots.remove(index)

			if (slot is ChestSlot) {
				inventory.clear(index)
			}
		}
	}


	override fun open(player: Player, backStack: MenuBackStack?) {
		close(player, false)

		try {
			backStack?.let { processBackStack(it) }

			val preOpenEvent = PlayerMenuPreOpenEvent(this, player)
			eventHandler.handlePreOpen(preOpenEvent)
			if (preOpenEvent.isCanceled) return

			scope.launch {
				delay(1.ticks)
				player.closeInventory()
				_views[player] = MenuView(this@StaticChestMenuImpl, player, inventory, backStack)

				player.openInventory(inventory)

				val openEvent = PlayerMenuOpenEvent(this@StaticChestMenuImpl, player, inventory)
				eventHandler.handleOpen(openEvent)

				if (updateDelay > Duration.ZERO && views.size == 1)
					setUpdateTask()
			}
		} catch (exception: Throwable) {
			exception.printStackTrace()
			removePlayer(player, true)
		}
	}

	override fun getInventory() = _inventory

	override fun setInventory(inventory: Inventory) {
		_inventory.storageContents = inventory.storageContents.map { it?.clone() }.toTypedArray()
	}
}