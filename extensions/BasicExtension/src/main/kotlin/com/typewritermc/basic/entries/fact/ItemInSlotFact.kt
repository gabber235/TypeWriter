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
    "item_in_slot_fact",
    "Check if a specific item is in a specific slot for the player",
    Colors.PURPLE,
    "fa6-solid:hand-holding"
)
/**
 * The `Item In Slot Fact` is a fact that returns the amount of a specific item the player has in a specific slot.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 * Check if the player is wearing a specific armor piece.
 */
class ItemInSlotFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    val item: Var<Item> = ConstVar(Item.Empty),
    val slot: Var<Int> = ConstVar(0),
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val itemInSlot = player.inventory.getItem(slot.get(player)) ?: return FactData(0)
        if (!item.get(player).isSameAs(player, itemInSlot)) return FactData(0)
        return FactData(itemInSlot.amount)
    }
}