package lirand.api.dsl.menu.builders.fixed

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.*
import lirand.api.dsl.menu.builders.MenuDSLEventHandler
import lirand.api.dsl.menu.exposed.*
import lirand.api.dsl.menu.exposed.fixed.StaticSlot
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.time.Duration

abstract class AbstractStaticMenuDSL<S : StaticSlot<I>, I : Inventory>(
	final override val plugin: Plugin,
	override var cancelEvents: Boolean
) : StaticMenuDSL<S, I> {

	protected val scope = CoroutineScope(
		plugin.minecraftDispatcher + SupervisorJob() +
				CoroutineExceptionHandler { _, exception -> exception.printStackTrace() }
	)

	override var updateDelay: Duration = Duration.ZERO
		set(value) {
			field = value.takeIf { it >= Duration.ZERO } ?: Duration.ZERO
			removeUpdateTask()
			if (value > Duration.ZERO && views.isNotEmpty())
				setUpdateTask()
		}


	protected val _views = WeakHashMap<Player, MenuView<I>>()
	override val views: Map<Player, MenuView<I>> get() = _views

	protected val _slots = TreeMap<Int, S>()
	override val slots: Map<Int, S> get() = _slots

	override val data = MenuTypedDataMap()
	override val playerData = MenuPlayerDataMap()

	override val eventHandler = MenuDSLEventHandler<I>(plugin)



	override fun setSlot(index: Int, slot: S) {
		if (index in rangeOfSlots)
			_slots[index] = slot
	}

	override fun removeSlot(index: Int) {
		_slots.remove(index)
	}

	override fun clearSlots() {
		_slots.clear()
	}

	override fun update() {
		for (view in views.values) {
			val updateEvent = PlayerMenuUpdateEvent(this, view.player, view.inventory)
			eventHandler.handleUpdate(updateEvent)

			for (index in rangeOfSlots) {
				val slot = getSlotOrBaseSlot(index)
				callSlotUpdateEvent(index, slot, view.player, view.inventory)
			}
		}
	}

	override fun updateSlot(slot: S) {
		val slotMatches = getSlotMatches(slot)

		for (view in views.values) {
			for ((index, slot) in slotMatches) {
				callSlotUpdateEvent(index, slot, view.player, view.inventory)
			}
		}
	}


	override fun close(player: Player, closeInventory: Boolean) {
		if (player !in _views) return

		val menuClose = PlayerMenuCloseEvent(this, player)
		eventHandler.handleClose(menuClose)

		removePlayer(player, closeInventory)

		if (updateDelay > Duration.ZERO && views.isEmpty())
			removeUpdateTask()
	}

	override fun back(player: Player, key: String?) {
		val backStack = views[player]?.backStack?.takeIf { it.isNotEmpty() } ?: return

		if (key != null) {
			if (backStack.none { it.key == key }) {
				close(player, true)
				return
			}

			while (backStack.peek().key != key) {
				backStack.pop()
			}
		}
		else {
			backStack.pop()
		}
		val frame = backStack.peek() ?: run {
			close(player, true)
			return
		}
		frame.menu.playerData[player].putAll(frame.playerData)
		backStack.next(pushToStack = false)
		frame.menu.open(player, backStack)
	}


	protected fun processBackStack(
		backStack: MenuBackStack,
		playerData: MenuTypedDataMap = MenuTypedDataMap(this.playerData[backStack.player])
	) {
		if (!backStack.nextPushToStack) {
			backStack.next(pushToStack = true)
			return
		}

		backStack.push(backStack.Frame(this, playerData))
	}



	protected fun removePlayer(player: Player, closeInventory: Boolean) {
		if (closeInventory) player.closeInventory()

		val viewing = _views.remove(player) != null
		if (viewing)
			clearPlayerData(player)
	}

	protected fun setUpdateTask() {
		scope.launch {
			while (isActive) {
				delay(updateDelay)
				update()
			}
		}
	}

	protected fun removeUpdateTask() {
		scope.coroutineContext.cancelChildren()
	}

	protected fun callSlotUpdateEvent(index: Int, slot: S, player: Player, inventory: I) {
		val slotUpdateEvent = PlayerMenuSlotUpdateEvent(this, index, slot, player, inventory)
		slot.eventHandler.handleUpdate(slotUpdateEvent)
	}

	protected fun getSlotMatches(slot: S): Map<Int, S> {
		return if (slot === baseSlot) {
			rangeOfSlots.mapNotNull { if (slots[it] == null || slots[it] === baseSlot) it to slot else null }.toMap()
		}
		else {
			rangeOfSlots.mapNotNull { if (slot === slots[it]) it to slot else null }.toMap()
		}
	}
}