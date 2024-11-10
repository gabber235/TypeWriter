package com.typewritermc.basic.entries.fact

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.facts.FactData
import com.typewritermc.engine.paper.utils.item.Item
import org.bukkit.entity.Player

@Entry(
    "item_holding_fact",
    "The amount of a specific item the player is currently holding",
    Colors.PURPLE,
    "fa6-solid:hand-holding"
)
/**
 * The `Item Holding Fact` is a fact that returns the amount of a specific item the player is currently holding.
 * When no properties for the item are set, the fact will return the amount of the item the player is holding.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 * Show PathStreams when the player is holding a specific item.
 * Like showing a path to a location when the player is holding a map.
 */
class ItemHoldingFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    val item: Var<Item> = ConstVar(Item.Empty),
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val holdingItem = player.inventory.itemInMainHand
        val amount = if (item.get(player).isSameAs(player, holdingItem)) holdingItem.amount else 0
        return FactData(amount)
    }
}