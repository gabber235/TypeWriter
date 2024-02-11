package com.caleb.typewriter.worldguard.entries.event

import com.caleb.typewriter.worldguard.RegionsEnterEvent
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry

@Entry("on_enter_region", "When a player enters a WorldGuard region", Colors.YELLOW, "fa6-solid:door-open")
/**
 * The `Enter Region Event` is triggered when a player enters a region.
 *
 * ## How could this be used?
 *
 * This event could be used to trigger a message to the player when they enter a region, like a welcome.
 * Or when they enter a region, it could trigger a quest to start and start a dialogue or cinematic.
 */
class EnterRegionEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The region to check for.")
    // The region that the player must enter to trigger the event.
    val region: String = "",
) : EventEntry

@EntryListener(EnterRegionEventEntry::class)
fun onEnterRegions(event: RegionsEnterEvent, query: Query<EnterRegionEventEntry>) {
    val player = server.getPlayer(event.player.uniqueId) ?: return
    query findWhere { it.region in event } triggerAllFor player
}

