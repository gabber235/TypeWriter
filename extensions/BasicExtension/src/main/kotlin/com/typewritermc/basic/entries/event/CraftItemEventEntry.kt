package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.utils.item.Item
import org.bukkit.entity.Player
import org.bukkit.event.inventory.CraftItemEvent

@Entry("craft_item_event", "Called when a player crafts an item", Colors.YELLOW, "mdi:hammer-wrench")
/**
 * The `Craft Item Event` is triggered when a player crafts an item.
 * This can be from a crafting table, a furnace, smiting table, campfire, or any other crafting method.
 *
 * ## How could this be used?
 * This could be used to complete a quest where the player has to craft a certain item, or to give the player a reward when they craft a certain item.
 */
class CraftItemEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val craftedItem: Item = Item.Empty,
): EventEntry

@EntryListener(CraftItemEventEntry::class)
fun onCraftItem(event: CraftItemEvent, query: Query<CraftItemEventEntry>) {
    val player = event.whoClicked
    if (player !is Player) return
    query.findWhere { it.craftedItem.isSameAs(player, event.recipe.result) } triggerAllFor player
}