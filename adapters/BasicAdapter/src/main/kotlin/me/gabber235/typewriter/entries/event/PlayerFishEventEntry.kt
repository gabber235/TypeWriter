package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.EntryListener
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.triggerAllFor
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.Item
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerFishEvent
import java.util.*

@Entry("on_player_fish", "When the a player caught a fish or an item", Colors.YELLOW, Icons.FISH)
/**
 * The `Player Fish Event` is triggered when a player catches a fish or an item.
 *
 * ## How could this be used?
 *
 * You can create custom fishing mechanics, such as catching a specific item when fishing in a specific location.
 */
class PlayerFishEventEntry (
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
    @Help("The location where the fish or item was caught.")
    val location: Optional<Location> = Optional.empty(),
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

@EntryListener(PlayerFishEventEntry::class)
fun onPlayerFish(event: PlayerFishEvent, query: Query<PlayerFishEventEntry>) {
    if (event.state != PlayerFishEvent.State.CAUGHT_FISH) return

    query findWhere { entry ->
        // Check if the player clicked on the correct location
        if (!entry.location.map { it == event.player.location }.orElse(true)) return@findWhere false

        // Check if the player is holding the correct item
        if (!hasItemInHand(event.player, entry.itemInHand)) return@findWhere false

        // Check if the player caught the correct item
        val caughtItem = event.caught as org.bukkit.entity.Item
        return@findWhere entry.caught.isSameAs(event.player, caughtItem.itemStack)
    } triggerAllFor event.player
}