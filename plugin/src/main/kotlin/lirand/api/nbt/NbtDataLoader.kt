package lirand.api.nbt

import lirand.api.extensions.server.craftBukkitPackage
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

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

private val craftItemStackClass =
    Class.forName("$craftBukkitPackage.inventory.CraftItemStack")

private val asNMSCopyMethod =
    craftItemStackClass.getMethod("asNMSCopy", ItemStack::class.java)

private val getItemMetaMethod = craftItemStackClass.methods
    .find { method ->
        method.name == "getItemMeta" && method.parameterTypes.let {
            it.size == 1 && it[0] == asNMSCopyMethod.returnType
        }
    }!!

private val minecraftItemStackClass = asNMSCopyMethod.returnType

private val minecraftItemStackTagField = minecraftItemStackClass.declaredFields
    .find { it.type == nbtCompoundClass }!!
    .apply { isAccessible = true }

private val minecraftItemStackSetTagMethod = minecraftItemStackClass.methods
    .find { method ->
        method.returnType == Void.TYPE && method.parameterTypes.let {
            it.size == 1 && it[0] == nbtCompoundClass
        }
    }!!

private val minecraftItemStackSaveMethod = minecraftItemStackClass.methods
    .find { method ->
        method.returnType == nbtCompoundClass && method.parameterTypes.let {
            it.size == 1 && it[0] == nbtCompoundClass
        }
    }!!