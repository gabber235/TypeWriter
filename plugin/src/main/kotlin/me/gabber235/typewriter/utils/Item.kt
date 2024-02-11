package me.gabber235.typewriter.utils

import lirand.api.extensions.inventory.meta
import lirand.api.nbt.NbtData
import lirand.api.nbt.tagNbtData
import me.gabber235.typewriter.adapters.modifiers.*
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

open class Item(
    @MaterialProperties(MaterialProperty.ITEM)
    @Icon("fa6-solid:cube")
    @Help("The material of the item.")
    private val material: Optional<Material> = Optional.empty(),
    @InnerMin(Min(0))
    @Icon("fa6-solid:hashtag")
    @Help("The amount of items.")
    val amount: Optional<Int> = Optional.empty(),
    @Placeholder
    @Colored
    @Icon("fa6-solid:tag")
    @Help("The display name of the item.")
    private val name: Optional<String> = Optional.empty(),
    @Placeholder
    @Colored
    @MultiLine
    @Icon("flowbite:file-lines-solid")
    @Help("The lore of the item.")
    private val lore: Optional<String> = Optional.empty(),
//    private val enchantments: Optional<Map<Enchantment, Int>>,
    @Icon("fa6-solid:flag")
    @Help("Special flags for the item.")
    private val flags: Optional<List<ItemFlag>> = Optional.empty(),
    @Icon("mingcute:code-fill")
    @Help("The serialized NBT data of the item.")
    private val nbt: Optional<String> = Optional.empty(),
) {

    companion object Empty : Item(
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
//        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
    )

    fun build(player: Player?): ItemStack {
        val item = ItemStack(material.orElse(Material.STONE), amount.orElse(1))
        // Nbt needs to be done first because it will not include the display name and lore.
        // Otherwise, it will overwrite the display name and lore.
        if (nbt.isPresent) {
            item.tagNbtData = NbtData(nbt.get())
        }
        item
            .meta<ItemMeta> {
                if (name.isPresent) {
                    displayName(name.get().parsePlaceholders(player).asMini())
                }
                if (this@Item.lore.isPresent) {
                    lore(this@Item.lore.get().parsePlaceholders(player).split("\n").map { it.asMini() })
                }
//            if (enchantments.isPresent) {
//                enchantments.get().forEach { (enchantment, level) ->
//                    addEnchant(enchantment, level, true)
//                }
//            }
                if (flags.isPresent) {
                    flags.get().forEach { flag ->
                        addItemFlags(flag)
                    }
                }

            }
        return item
    }

    @OptIn(ExperimentalContracts::class)
    fun isSameAs(player: Player?, item: ItemStack?): Boolean {
        contract {
            returns(true) implies (item != null)
        }
        if (item == null) return false
        if (material.isPresent && item.type != material.get()) return false
        if (amount.isPresent && item.amount != amount.get()) return false
        if (name.isPresent && item.itemMeta?.displayName()?.asMini() != name.get()
                .parsePlaceholders(player)
        ) return false
        if (lore.isPresent) {
            val lore = item.itemMeta?.lore()?.joinToString("\n") { it.asMini() } ?: ""
            if (this.lore.get().parsePlaceholders(player) != lore) return false
        }
//        if (enchantments.isPresent && item.itemMeta?.enchants != enchantments.get()) return false
        if (flags.isPresent && item.itemMeta?.itemFlags?.toList() != flags.get()) return false
        if (nbt.isPresent && item.strippedTagNbtData.toString() != nbt.get()) return false
        return true
    }

    fun copy(
        material: Optional<Material>? = null,
        amount: Optional<Int>? = null,
        name: Optional<String>? = null,
        lore: Optional<String>? = null,
//        enchantments: Optional<Map<Enchantment, Int>>? = null,
        flags: Optional<List<ItemFlag>>? = null,
        nbt: Optional<String>? = null,
    ): Item {
        return Item(
            material = material ?: this.material,
            amount = amount ?: this.amount,
            name = name ?: this.name,
            lore = lore ?: this.lore,
//            enchantments = enchantments ?: this.enchantments,
            flags = flags ?: this.flags,
            nbt = nbt ?: this.nbt,
        )
    }
}

fun ItemStack.toItem(): Item {
    val nbt = strippedTagNbtData
    return Item(
        material = Optional.ofNullable(type),
        amount = Optional.ofNullable(amount),
        name = Optional.ofNullable(itemMeta?.displayName()?.asMini()),
        lore = Optional.ofNullable(itemMeta?.lore()?.joinToString("\n") { it.asMini() }),
//        enchantments = Optional.ofNullable(itemMeta?.enchants),
        flags = if (itemMeta?.itemFlags?.isNotEmpty() == true) Optional.ofNullable(itemMeta?.itemFlags?.toList()) else Optional.empty(),
        nbt = if (nbt.keys.isNotEmpty()) Optional.ofNullable(nbt.toString()) else Optional.empty(),
    )
}

/// Returns the NBT data of the item without the display name and lore.
val ItemStack.strippedTagNbtData: NbtData
    get() {
        val nbt = tagNbtData
        nbt -= "display"
        return nbt
    }