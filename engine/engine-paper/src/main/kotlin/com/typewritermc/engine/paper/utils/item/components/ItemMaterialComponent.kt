package com.typewritermc.engine.paper.utils.item.components

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.AlgebraicTypeInfo
import com.typewritermc.core.extension.annotations.MaterialProperties
import com.typewritermc.core.extension.annotations.MaterialProperty
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@AlgebraicTypeInfo("material", Colors.BLUE, "fa6-solid:cube")
class ItemMaterialComponent(
    @MaterialProperties(MaterialProperty.ITEM)
    val material: Material = Material.STONE,
) : ItemComponent {
    override fun apply(player: Player?, item: ItemStack) {
        item.type = material
    }
}