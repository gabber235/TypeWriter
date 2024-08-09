package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Item
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerFishEvent
import java.util.*

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
    @Help("The item that the player caught.")
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