package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.utils.Item
import com.typewritermc.engine.paper.utils.ThreadType
import org.bukkit.entity.Player
import java.util.*

@Entry("remove_item", "Remove an item from the players inventory", Colors.RED, "icomoon-free:user-minus")
/**
 * The `Remove Item Action` is an action that removes an item from the player's inventory.
 * This action provides you with the ability to remove items from the player's inventory in response to specific events.
 * <Admonition type="caution">
 *     This action will try to remove "as much as possible" but does not verify if the player has enough items in their inventory.
 *     If you want to guarantee that the player has enough items in their inventory, add an
 *     <Link to='../fact/inventory_item_count_fact'>Inventory Item Count Fact</Link> to the criteria.
 * </Admonition>
 *
 * ## How could this be used?
 *
 * This can be used when `giving` an NPC an item, and you want to remove the item from the player's inventory.
 * Or when you want to remove a key from the player's inventory after they use it to unlock a door.
 */
class RemoveItemActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val item: Item = Item.Empty,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        // Because item.build() can return a identical looking item but with different data,
        // we need to compare the items

        val itemWithoutAmount = item.copy(amount = Optional.empty())

        val items = player.inventory.contents.withIndex().filter {
            itemWithoutAmount.isSameAs(player, it.value)
        }.iterator()

        var toRemove = item.amount.orElse(1)

        ThreadType.SYNC.launch {
            while (toRemove > 0 && items.hasNext()) {
                val (index, item) = items.next()
                if (item == null) continue
                val amount = item.amount
                if (amount > toRemove) {
                    item.amount = amount - toRemove
                    player.inventory.setItem(index, item)
                    break
                } else {
                    toRemove -= amount
                    player.inventory.setItem(index, null)
                }
            }
        }
    }
}