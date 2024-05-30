package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.FactData
import me.gabber235.typewriter.utils.Item
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
    @Help("The item to check for.")
    val item: Item = Item.Empty,
    @Help("The slot to check.")
    val slot: Int = 0,
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val itemInSlot = player.inventory.getItem(slot) ?: return FactData(0)
        if (!item.isSameAs(player, itemInSlot)) return FactData(0)
        return FactData(itemInSlot.amount)
    }
}