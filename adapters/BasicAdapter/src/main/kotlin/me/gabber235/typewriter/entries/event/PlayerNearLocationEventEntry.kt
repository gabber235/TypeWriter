package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Min
import me.gabber235.typewriter.entry.EntryListener
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.startDialogueWithOrNextDialogue
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent

@Entry("on_player_near_location", "When the player is near a certain location", Colors.YELLOW, Icons.HAND_POINTER)
/**
 * The `PlayerNearLocationEventEntry` class represents an event that is triggered when a player is within a certain range of a location.
 *
 * ## How could this be used?
 *
 * This could be used to create immersive gameplay experiences such as triggering a special event or dialogue when a player approaches a specific location.
 * For example, when a player gets close to a hidden treasure, a hint could be revealed or a guardian could spawn.
 */
class PlayerNearLocationEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
    @Help("The location the player should be near.")
    val location: Location = Location(null, 0.0, 0.0, 0.0),
    @Help("The range within which the event should trigger.")
    @Min(1)
    val range: Double = 1.0
) : EventEntry

@EntryListener(PlayerNearLocationEventEntry::class)
fun onPlayerNearLocation(event: PlayerMoveEvent, query: Query<PlayerNearLocationEventEntry>) {
    // Only check if the player moved a block
    if (event.from.blockX == event.to.blockX && event.from.blockY == event.to.blockY && event.from.blockZ == event.to.blockZ) return

    query findWhere { entry ->
        event.player.isInRange(entry.location, entry.range)
    } startDialogueWithOrNextDialogue event.player
}

fun Player.isInRange(location: Location, range: Double): Boolean {
    return this.location.distanceSquared(location) <= range * range
}