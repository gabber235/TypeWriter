package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Min
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.utils.toPosition
import lirand.api.extensions.math.blockLocation
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerTeleportEvent

@Entry("on_player_near_location", "When the player is near a certain location", Colors.YELLOW, "mdi:map-marker-radius")
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
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val location: Position = Position.ORIGIN,
    @Help("How close the player must be to the location to trigger the event.")
    @Min(1)
    val range: Double = 1.0
) : EventEntry

@EntryListener(PlayerNearLocationEventEntry::class)
fun onPlayerNearLocation(event: PlayerMoveEvent, query: Query<PlayerNearLocationEventEntry>) {
    // Only check if the player moved a block
    if (!event.hasChangedBlock()) return
    val fromPosition = event.from.blockLocation.toPosition()
    val toPosition = event.to.blockLocation.toPosition()
    query.findInRange(fromPosition, toPosition).triggerAllFor(event.player)
}

@EntryListener(PlayerNearLocationEventEntry::class)
fun onPlayerTeleportNearLocation(event: PlayerTeleportEvent, query: Query<PlayerNearLocationEventEntry>) {
    val fromPosition = event.from.blockLocation.toPosition()
    val toPosition = event.to.blockLocation.toPosition()
    query.findInRange(fromPosition, toPosition).triggerAllFor(event.player)
}

private fun Query<PlayerNearLocationEventEntry>.findInRange(
    from: Position,
    to: Position,
): Sequence<PlayerNearLocationEventEntry> {
    return findWhere { entry ->
        !from.isInRange(entry.location, entry.range) && to.isInRange(entry.location, entry.range)
    }
}