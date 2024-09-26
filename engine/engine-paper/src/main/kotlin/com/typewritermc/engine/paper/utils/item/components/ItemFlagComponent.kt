package com.typewritermc.engine.paper.utils.item.components

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.AlgebraicTypeInfo
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

@AlgebraicTypeInfo("flag", Colors.BLUE, "material-symbols:flag")
class ItemFlagComponent(
    val flag: ItemFlag,
) : ItemComponent {
    override fun apply(player: Player?, item: ItemStack) = item.addItemFlags(flag)
}