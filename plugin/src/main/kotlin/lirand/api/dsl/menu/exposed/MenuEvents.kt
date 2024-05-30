package lirand.api.dsl.menu.exposed

import lirand.api.dsl.menu.exposed.fixed.StaticMenu
import org.bukkit.entity.Player
import org.bukkit.inventory.AnvilInventory
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface PlayerMenuEvent {
	val menu: StaticMenu<*, *>
	val player: Player

	val currentPlayerData: MenuTypedDataMap
		get() = menu.playerData[player]

	val backStack: MenuBackStack?
		get() = menu.views[player]?.backStack


	fun saveAllDataToBackStack() {
		val frame = backStack?.peek()?.takeIf { it.menu == menu } ?: return
		frame.playerData.putAll(currentPlayerData)
	}

	fun saveDataToBackStack(dataKey: String, vararg dataKeys: String) {
		val frame = backStack?.peek()?.takeIf { it.menu == menu } ?: return
		frame.playerData.putAll(setOf(dataKey, *dataKeys).associateWith { currentPlayerData[it] })
	}

	fun saveDataToBackStack(dataKeys: Collection<String>) {
		val frame = backStack?.peek()?.takeIf { it.menu == menu } ?: return
		frame.playerData.putAll(dataKeys.associateWith { currentPlayerData[it] })
	}

	fun saveDataToBackStack(dataPair: Pair<String, Any?>, vararg dataPairs: Pair<String, Any?>) {
		val frame = backStack?.peek()?.takeIf { it.menu == menu } ?: return
		frame.playerData.putAll(mapOf(dataPair, *dataPairs))
	}

	fun saveDataToBackStack(data: Map<String, Any?>) {
		val frame = backStack?.peek()?.takeIf { it.menu == menu } ?: return
		frame.playerData.putAll(data)
	}

	fun back(key: String? = null) {
		menu.back(player, key)
	}
}

internal fun PlayerMenuEvent(
	menu: StaticMenu<*, *>,
	player: Player
): PlayerMenuEvent = PlayerMenuEventImpl(menu, player)

internal class PlayerMenuEventImpl(
	override val menu: StaticMenu<*, *>,
	override val player: Player
) : PlayerMenuEvent



interface PlayerInventoryMenuEvent<I : Inventory> : PlayerMenuEvent {
	val inventory: I

	fun close() {
		if (player.openInventory.topInventory.holder == menu) {
			player.closeInventory()
		}
	}
}

internal fun <I : Inventory> PlayerInventoryMenuEvent(
	menu: StaticMenu<*, *>,
	player: Player,
	inventory: I
): PlayerInventoryMenuEvent<I> =
	PlayerInventoryMenuImpl(
		menu, player, inventory
	)

internal class PlayerInventoryMenuImpl<I : Inventory>(
	override val menu: StaticMenu<*, *>,
	override val player: Player,
	override val inventory: I
) : PlayerInventoryMenuEvent<I>



interface PlayerMenuCancellableEvent : PlayerMenuEvent {
	var isCanceled: Boolean
}


open class PlayerMenuInteractEvent<I : Inventory>(
	override val menu: StaticMenu<*, *>,
	override val player: Player,
	override val inventory: I,
	override var isCanceled: Boolean
) : PlayerInventoryMenuEvent<I>, PlayerMenuCancellableEvent

class PlayerMenuPreOpenEvent(
	override val menu: StaticMenu<*, *>,
	override val player: Player,
	override var isCanceled: Boolean = false
) : PlayerMenuEvent, PlayerMenuCancellableEvent

class PlayerMenuOpenEvent<I : Inventory>(
	override val menu: StaticMenu<*, *>,
	override val player: Player,
	override val inventory: I
) : PlayerInventoryMenuEvent<I>

class PlayerMenuUpdateEvent<I : Inventory>(
	override val menu: StaticMenu<*, *>,
	override val player: Player,
	override val inventory: I
) : PlayerInventoryMenuEvent<I>

class PlayerMenuCloseEvent(
	override val menu: StaticMenu<*, *>,
	override val player: Player
) : PlayerMenuEvent

interface PlayerMenuMoveEvent : PlayerMenuCancellableEvent {
	val movedItem: ItemStack?
}

class PlayerMoveToMenuEvent<I : Inventory>(
	menu: StaticMenu<*, *>,
	player: Player,
	inventory: I,
	isCanceled: Boolean,
	override val movedItem: ItemStack?,
	val hotbarKey: Int
) : PlayerMenuInteractEvent<I>(menu, player, inventory, isCanceled), PlayerMenuMoveEvent