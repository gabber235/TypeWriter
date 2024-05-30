package lirand.api.dsl.menu.builders.dynamic.chest

import com.github.shynixn.mccoroutine.bukkit.ticks
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lirand.api.dsl.menu.builders.dynamic.AbstractMenuDSL
import lirand.api.dsl.menu.builders.dynamic.SlotDSLEventHandler
import lirand.api.dsl.menu.builders.dynamic.chest.slot.ChestSlot
import lirand.api.dsl.menu.exposed.*
import lirand.api.dsl.menu.exposed.dynamic.Slot
import lirand.api.dsl.menu.exposed.MenuBackStack
import lirand.api.dsl.menu.exposed.MenuView
import lirand.api.extensions.inventory.Inventory
import lirand.api.extensions.inventory.clone
import lirand.api.extensions.inventory.set
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin
import kotlin.time.Duration

class ChestMenuImpl(
	plugin: Plugin,
	override var lines: Int,
	cancelEvents: Boolean
) : AbstractMenuDSL<Slot<Inventory>, Inventory>(plugin, cancelEvents), ChestMenuDSL {

	override val rangeOfSlots: IntRange = 0 until lines * 9

	override var baseSlot: Slot<Inventory> = ChestSlot(plugin, null, cancelEvents, SlotDSLEventHandler(plugin))


	override fun open(player: Player, backStack: MenuBackStack?) {
		close(player, false)

		try {
			backStack?.let { processBackStack(it) }

			val inventory = inventory.clone(
				false, title = title ?: dynamicTitle?.invoke(PlayerMenuEvent(this, player))
			)

			val preOpenEvent = PlayerMenuPreOpenEvent(this, player)
			eventHandler.handlePreOpen(preOpenEvent)
			if (preOpenEvent.isCanceled) return

			scope.launch {
				delay(1.ticks)
				player.closeInventory()
				_views[player] = MenuView(this@ChestMenuImpl, player, inventory, backStack)

				for (index in rangeOfSlots) {
					val slot = getSlotOrBaseSlot(index)
					val render = PlayerMenuSlotRenderEvent(this@ChestMenuImpl, index, slot, player, inventory)
					slot.eventHandler.handleRender(render)
				}

				player.openInventory(inventory)

				val openEvent = PlayerMenuOpenEvent(this@ChestMenuImpl, player, inventory)
				eventHandler.handleOpen(openEvent)

				if (updateDelay > Duration.ZERO && views.size == 1)
					setUpdateTask()
			}

		} catch (exception: Throwable) {
			exception.printStackTrace()
			removePlayer(player, true)
		}
	}

	override fun getInventory(): Inventory {
		val inventory = Inventory(rangeOfSlots.last + 1, this)

		for (index in rangeOfSlots) {
			val slot = getSlotOrBaseSlot(index)
			val item = slot.item?.clone()
			inventory[index] = item
		}

		return inventory
	}
}