package lirand.api.extensions.inventory

import lirand.api.extensions.server.server
import me.gabber235.typewriter.utils.asMini
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

fun Inventory(
	type: InventoryType,
	owner: InventoryHolder? = null,
	title: String? = null
): Inventory {
	return if (title != null)
		server.createInventory(owner, type, title.asMini())
	else
		server.createInventory(owner, type)
}

fun Inventory(
	size: Int,
	owner: InventoryHolder? = null,
	title: String? = null
): Inventory {
	return if (title != null)
		server.createInventory(owner, size, title.asMini())
	else
		server.createInventory(owner, size)
}


val Inventory.hasSpace: Boolean
	get() = contents.any { it == null || it.type == Material.AIR }

fun Inventory.hasSpace(
	itemStack: ItemStack,
	amount: Int = itemStack.amount
): Boolean = getSpaceOf(itemStack) >= amount

fun Inventory.getSpaceOf(itemStack: ItemStack): Int {
	return contents.filterNotNull().map {
		if (it.amount < it.maxStackSize && it.isSimilar(itemStack))
			it.maxStackSize - it.amount
		else 0
	}.count()
}


operator fun Inventory.get(slot: Int): ItemStack? = getItem(slot)

operator fun Inventory.set(slot: Int, itemStack: ItemStack?) {
	setItem(slot, itemStack)
}


fun Inventory.clone(
	cloneItemStacks: Boolean = true,
	owner: InventoryHolder? = holder,
	title: String? = null
): Inventory {
	val inventory = if (type == InventoryType.CHEST)
		Inventory(size, owner, title)
	else
		Inventory(type, owner, title)

	inventory.contents = if (cloneItemStacks)
		contents.map { it?.clone() }.toTypedArray()
	else
		contents


	return inventory
}