package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.utils.item.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerFishEvent

@Entry("on_fish", "When the a player caught a fish or an item", Colors.YELLOW, "mdi:fish")
/**
 * The `Player Fish Event` is triggered when a player catches a fish or an item.
 *
 * ## How could this be used?
 * You can create custom fishing mechanics, such as catching a specific item when fishing in a specific location.
 */
class FishEventEntry (
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The item the player must be holding when the fish or item is caught.")
    val itemInHand: Item = Item.Empty,
    val caught: Item = Item.Empty,
) : EventEntry


private fun hasItemInHand(player: Player, item: Item): Boolean {
    return item.isSameAs(player, player.inventory.itemInMainHand) || item.isSameAs(
        player,
        player.inventory.itemInOffHand
    )
}

@EntryListener(FishEventEntry::class)
fun onPlayerFish(event: PlayerFishEvent, query: Query<FishEventEntry>) {
    if (event.state != PlayerFishEvent.State.CAUGHT_FISH) return

    query findWhere { entry ->
        // Check if the player is holding the correct item
        if (!hasItemInHand(event.player, entry.itemInHand)) return@findWhere false

        // Check if the player caught the correct item
        val caughtItem = event.caught as? org.bukkit.entity.Item ?: return@findWhere false
        return@findWhere entry.caught.isSameAs(event.player, caughtItem.itemStack)
    } triggerAllFor event.player
}