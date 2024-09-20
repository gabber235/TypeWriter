package com.typewritermc.engine.paper.utils.item

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.AlgebraicTypeInfo
import com.typewritermc.engine.paper.utils.item.components.*
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@AlgebraicTypeInfo("custom_item", Colors.BLUE, "mdi:shape")
class CustomItem(
    private val components: List<ItemComponent> = emptyList(),
) : Item {
    @delegate:Transient
    private val itemStack: ItemStack by lazy(LazyThreadSafetyMode.NONE) {
        val itemStack = ItemStack(Material.AIR, 1)
        components.forEach { it.apply(null, itemStack) }
        itemStack
    }

    override fun build(player: Player?): ItemStack = itemStack
    override fun isSameAs(player: Player?, item: ItemStack?): Boolean = this.itemStack.isSimilar(item)
}