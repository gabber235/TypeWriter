package com.typewritermc.engine.paper.content.components

import com.typewritermc.engine.paper.content.ContentComponent
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Offer a way to have items in the player's inventory.
 */
interface ItemsComponent {
    fun items(player: Player): Map<Int, IntractableItem>
}

interface ItemComponent : ContentComponent, ItemsComponent {
    fun item(player: Player): Pair<Int, IntractableItem>

    override fun items(player: Player): Map<Int, IntractableItem> {
        val (slot, item) = item(player)
        return mapOf(slot to item)
    }

    override suspend fun initialize(player: Player) {}
    override suspend fun tick(player: Player) {}
    override suspend fun dispose(player: Player) {}
}

infix fun ItemStack.onInteract(action: (ItemInteraction) -> Unit) = IntractableItem(this, action)
infix operator fun ItemStack.invoke(action: (ItemInteraction) -> Unit) = IntractableItem(this, action)

data class IntractableItem(
    val item: ItemStack,
    val action: (ItemInteraction) -> Unit,
)

data class ItemInteraction(
    val type: ItemInteractionType,
    val slot: Int,
    val clickedBlock: Block?,
)

enum class ItemInteractionType {
    INVENTORY_CLICK,
    LEFT_CLICK,
    RIGHT_CLICK,
    SHIFT_LEFT_CLICK,
    SHIFT_RIGHT_CLICK,
    DROP,
    SWAP,
    ;

    val isClick: Boolean
        get() = this == LEFT_CLICK || this == RIGHT_CLICK || this == SHIFT_LEFT_CLICK || this == SHIFT_RIGHT_CLICK
}

