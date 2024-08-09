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
    @Help("The item to check for.")
    val item: Item = Item.Empty,
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val holdingItem = player.inventory.itemInMainHand
        val amount = if (item.isSameAs(player, holdingItem)) holdingItem.amount else 0
        return FactData(amount)
    }
}