package com.typewritermc.basic.entries.fact

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.facts.FactData
import com.typewritermc.engine.paper.utils.item.Item
import org.bukkit.entity.Player

@Entry(
    "inventory_item_count_fact",
    "The amount of a specific item in the player's inventory",
    Colors.PURPLE,
    "fa6-solid:bag-shopping"
)
/**
 * The `Inventory Item Count Fact` is a fact that returns the amount of a specific item in the player's inventory.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 *
 * This could be used to check if the player has a specific item in their inventory, or to check if they have a specific amount of an item.
 * Like giving the player a quest to collect 10 apples, and then checking if they have 10 apples in their inventory.
 */
class InventoryItemCountFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    val item: Var<Item> = ConstVar(Item.Empty),
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val item = item.get(player)
        val amount = player.inventory.contents.filterNotNull().filter { item.isSameAs(player, it) }.sumOf { it.amount }
        val amountInCursor = if (item.isSameAs(player, player.itemOnCursor)) player.itemOnCursor.amount else 0
        return FactData(amount + amountInCursor)
    }
}