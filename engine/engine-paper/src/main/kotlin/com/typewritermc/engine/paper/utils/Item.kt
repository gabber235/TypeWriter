package com.typewritermc.engine.paper.utils

import com.typewritermc.core.extension.annotations.*
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

open class Item(
    @MaterialProperties(MaterialProperty.ITEM)
    @Icon("fa6-solid:cube")
    private val material: Optional<Material> = Optional.empty(),
    @InnerMin(Min(0))
    @Icon("fa6-solid:hashtag")
    val amount: Optional<Int> = Optional.empty(),
    @Placeholder
    @Colored
    @Icon("fa6-solid:tag")
    private val name: Optional<String> = Optional.empty(),
    @Placeholder
    @Colored
    @MultiLine
    @Icon("flowbite:file-lines-solid")
    private val lore: Optional<String> = Optional.empty(),
//    private val enchantments: Optional<Map<Enchantment, Int>>,
    @Icon("material-symbols:flag")
    private val flags: Optional<List<ItemFlag>> = Optional.empty(),
) {

    companion object Empty : Item(
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
        Optional.empty(),
//        Optional.empty(),
        Optional.empty(),
    )

    fun build(player: Player?): ItemStack {
        val material = material.orElse(Material.STONE)
        if (material == Material.AIR) return ItemStack.empty()
        val item = ItemStack(material, amount.orElse(1))
        item
            .editMeta { meta ->
                if (this@Item.name.isPresent) {
                    meta.displayName(this@Item.name.get().parsePlaceholders(player).asMini())
                }
                if (this@Item.lore.isPresent) {
                    meta.lore(this@Item.lore.get().parsePlaceholders(player).split("\n").map { it.asMini() })
                }
//            if (enchantments.isPresent) {
//                enchantments.get().forEach { (enchantment, level) ->
//                    addEnchant(enchantment, level, true)
//                }
//            }
                if (flags.isPresent) {
                    flags.get().forEach { flag ->
                        meta.addItemFlags(flag)
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
        return true
    }

    fun copy(
        material: Optional<Material>? = null,
        amount: Optional<Int>? = null,
        name: Optional<String>? = null,
        lore: Optional<String>? = null,
//        enchantments: Optional<Map<Enchantment, Int>>? = null,
        flags: Optional<List<ItemFlag>>? = null,
    ): Item {
        return Item(
            material = material ?: this.material,
            amount = amount ?: this.amount,
            name = name ?: this.name,
            lore = lore ?: this.lore,
//            enchantments = enchantments ?: this.enchantments,
            flags = flags ?: this.flags,
        )
    }
}

fun ItemStack.toItem(): Item {
    return Item(
        material = Optional.ofNullable(type),
        amount = Optional.ofNullable(amount),
        name = Optional.ofNullable(itemMeta?.displayName()?.asMini()),
        lore = Optional.ofNullable(itemMeta?.lore()?.joinToString("\n") { it.asMini() }),
//        enchantments = Optional.ofNullable(itemMeta?.enchants),
        flags = if (itemMeta?.itemFlags?.isNotEmpty() == true) Optional.ofNullable(itemMeta?.itemFlags?.toList()) else Optional.empty(),
    )
}