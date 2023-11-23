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
    @Icon(Icons.CUBE)
    @Help("The material of the item.")
    private val material: Optional<Material> = Optional.empty(),
    @InnerMin(Min(0))
    @Icon(Icons.HASHTAG)
    @Help("The amount of items.")
    private val amount: Optional<Int> = Optional.empty(),
    @Placeholder
    @Colored
    @Icon(Icons.TAG)
    @Help("The display name of the item.")
    private val name: Optional<String> = Optional.empty(),
    @Placeholder
    @Colored
    @MultiLine
    @Icon(Icons.SOLID_FILE_LINES)
    @Help("The lore of the item.")
    private val lore: Optional<String> = Optional.empty(),
//    private val enchantments: Optional<Map<Enchantment, Int>>,
    @Icon(Icons.SOLID_FLAG)
    @Help("Special flags for the item.")
    private val flags: Optional<List<ItemFlag>> = Optional.empty(),
    @Icon(Icons.CODE)
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
        val item = ItemStack(material.orElse(Material.STONE), amount.orElse(1)).meta<ItemMeta> {
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
        if (nbt.isPresent) {
            item.tagNbtData = NbtData(nbt.get())
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
        if (name.isPresent && item.itemMeta?.displayName() != name.get().parsePlaceholders(player)
                .asMini()
        ) return false
        if (lore.isPresent) {
            val lore = lore.get().parsePlaceholders(player).split("\n").map { it.asMini() }
            if (item.itemMeta?.lore() != lore) return false
        }
//        if (enchantments.isPresent && item.itemMeta?.enchants != enchantments.get()) return false
        if (flags.isPresent && item.itemMeta?.itemFlags?.toList() != flags.get()) return false
        if (nbt.isPresent && item.tagNbtData.toString() != nbt.get()) return false
        return true
    }
}

fun ItemStack.toItem(): Item {
    val nbt = tagNbtData
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