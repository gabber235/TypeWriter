package lirand.api.nbt

import lirand.api.extensions.inventory.Inventory
import lirand.api.extensions.inventory.ItemStack
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

fun Inventory.getSerializableNbtData(title: String? = null): NbtData {
	return nbtData {
		tag["type"] = type.toString().lowercase()

		if (type == InventoryType.CHEST)
			tag["size"] = size

		if (title != null)
			tag["title"] = title

		tag["Items"] = mapIndexedNotNull { index, itemStack ->
			itemStack?.let {
				nbtData {
					tag["Slot"] = index.toByte()
					tag["ItemStack"] = itemStack.nbtData
				}
			}
		}
	}
}


fun NbtData.deserializeItemStack(): ItemStack {
	return ItemStack(
		Material.matchMaterial(tag["id"]) ?: error("Invalid material id"),
		tag.getOrDefault<Byte>("Count") { 1 }.toInt(),
		tag.get<NbtData>("tag"),
	)
}

fun NbtData.deserializeInventory(
	owner: InventoryHolder? = null,
	title: String? = null
): Inventory {
	val type = InventoryType.valueOf(tag.get<String>("type").uppercase())

	val size = if (type == InventoryType.CHEST) tag.get<Int>("size") else -1

	val resultTitle = title ?: tag.getOrNull<String>("title")

	val resultInventory = if (type == InventoryType.CHEST)
		Inventory(size, owner, resultTitle)
	else
		Inventory(type, owner, resultTitle)

	val itemStacksNbt = tag.get<List<NbtData>>("Items")

	return resultInventory.apply {
		for (itemStackNbt in itemStacksNbt) {
			setItem(
				itemStackNbt.tag.getOrNull<Byte>("Slot")?.toInt() ?: continue,
				itemStackNbt.tag.getOrNull<NbtData>("ItemStack")?.deserializeItemStack()
			)
		}
	}
}

