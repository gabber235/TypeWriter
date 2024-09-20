package com.typewritermc.engine.paper.utils.item

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

sealed interface Item {
    fun build(player: Player?): ItemStack
    fun isSameAs(player: Player?, item: ItemStack?): Boolean

    companion object {
        val Empty = CustomItem()
    }
}
