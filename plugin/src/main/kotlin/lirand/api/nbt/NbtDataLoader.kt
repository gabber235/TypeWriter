package lirand.api.nbt

import lirand.api.extensions.server.nmsNumberVersion
import lirand.api.extensions.server.nmsVersion
import org.bukkit.block.TileState
import org.bukkit.entity.Entity
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

var Entity.nbtData: NbtData
	get() {
		val nbtTagCompound = nbtCompoundConstructor.newInstance()

		val entity = craftEntityGetHandleMethod.invoke(this)

		minecraftEntitySaveMethod.invoke(entity, nbtTagCompound)

		return NbtData(nbtTagCompound)
	}
	set(value) {
		val entity = craftEntityGetHandleMethod.invoke(this)

		minecraftEntityLoadMethod.invoke(entity, value.nbtTagCompound)
	}


val ItemStack.nbtData: NbtData
	get() {
		val itemStack = asNMSCopyMethod.invoke(null, this)

		val nbtTagCompound = nbtCompoundConstructor.newInstance()
		return NbtData(minecraftItemStackSaveMethod.invoke(itemStack, nbtTagCompound))
	}


var ItemStack.tagNbtData: NbtData
	get() {
		val itemStack = asNMSCopyMethod.invoke(null, this)

		return NbtData(minecraftItemStackTagField.get(itemStack))
	}
	set(value) {
		val nmsItemStack = (asNMSCopyMethod.invoke(null, this)).apply {
			minecraftItemStackSetTagMethod.invoke(this, value.nbtTagCompound)
		}
		itemMeta = getItemMetaMethod.invoke(null, nmsItemStack) as ItemMeta
	}


var TileState.nbtData: NbtData
	get() {
		val nbtTagCompound = tileStateGetSnapshotNBTMethod.invoke(this)

		return NbtData(nbtTagCompound)
	}
	set(value) {
		val snapshot = tileStateGetSnapshotMethod.invoke(this)

		if (nmsNumberVersion < 17) {
			blockEntityLoadMethod.invoke(
				snapshot, tileStateGetHandleMethod.invoke(this), value.nbtTagCompound
			)
		}
		else {
			blockEntityLoadMethod.invoke(snapshot, value.nbtTagCompound)
		}
	}



private val craftItemStackClass =
	Class.forName("org.bukkit.craftbukkit.v$nmsVersion.inventory.CraftItemStack")

private val craftEntityGetHandleMethod =
	Class.forName("org.bukkit.craftbukkit.v$nmsVersion.entity.CraftEntity")
		.getMethod("getHandle")


private val asNMSCopyMethod =
	craftItemStackClass.getMethod("asNMSCopy", ItemStack::class.java)

private val getItemMetaMethod = craftItemStackClass.methods
	.find {
		it.name == "getItemMeta" && it.parameterTypes.let {
			it.size == 1 && it[0] == asNMSCopyMethod.returnType
		}
	}!!


private val tileStateClass =
	Class.forName("org.bukkit.craftbukkit.v$nmsVersion.block.CraftBlockEntityState")

private val tileStateGetSnapshotMethod = tileStateClass.getDeclaredMethod("getSnapshot")
	.apply { isAccessible = true }

private val tileStateGetSnapshotNBTMethod = tileStateClass.getMethod("getSnapshotNBT")



private val minecraftEntityClass = craftEntityGetHandleMethod.returnType

private val minecraftEntityLoadMethod = minecraftEntityClass.methods
	.find {
		it.returnType == Void.TYPE && it.parameterTypes.let {
			it.size == 1 && it[0] == nbtCompoundClass
		}
	}!!
private val minecraftEntitySaveMethod = minecraftEntityClass.methods
	.find {
		it.returnType == nbtCompoundClass && it.parameterTypes.let {
			it.size == 1 && it[0] == nbtCompoundClass
		}
	}!!


private val minecraftItemStackClass = asNMSCopyMethod.returnType

private val minecraftItemStackTagField = minecraftItemStackClass.declaredFields
	.find { it.type == nbtCompoundClass }!!
	.apply { isAccessible = true }

private val minecraftItemStackSetTagMethod = minecraftItemStackClass.methods
	.find {
		it.returnType == Void.TYPE && it.parameterTypes.let {
			it.size == 1 && it[0] == nbtCompoundClass
		}
	}!!

private val minecraftItemStackSaveMethod = minecraftItemStackClass.methods
	.find {
		it.returnType == nbtCompoundClass && it.parameterTypes.let {
			it.size == 1 && it[0] == nbtCompoundClass
		}
	}!!


private val blockEntityClass = tileStateGetSnapshotMethod.returnType

private val tileStateGetHandleMethod = tileStateClass.getMethod("getHandle")

private val blockEntityLoadMethod = blockEntityClass.methods.let {
	if (nmsNumberVersion < 17) {
		it.find {
			it.returnType == Void.TYPE && it.parameterTypes.let {
				it.size == 2 && it[0] == tileStateGetHandleMethod.returnType && it[1] == nbtCompoundClass
			}
		}
	}
	else {
		it.find {
			it.returnType == Void.TYPE && it.parameterTypes.let {
				it.size == 1 && it[0] == nbtCompoundClass
			}
		}
	}
}!!