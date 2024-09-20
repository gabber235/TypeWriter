package com.typewritermc.engine.paper.utils.item.components

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

sealed interface ItemComponent {
    fun apply(player: Player?, item: ItemStack)
}