package com.caleb.typewriter.worldguard.entries.event

import com.caleb.typewriter.worldguard.RegionsExitEvent
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry

@Entry("on_exit_region", "When a player exits a WorldGuard region", Colors.YELLOW, "fa6-solid:door-closed")
/**
 * The `Exit Region Event` is triggered when a player leaves a region.
 *
 * ## How could this be used?
 *
 * This event could be used to trigger a message when a player leaves a region, and give them a farewell message.
 * Or if you wanted to make a region that is a "safe zone" where players can't be attacked, you could use this event to trigger a message when a player leaves the region.
 */
class ExitRegionEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The region to check for.")
    // The region the player must leave to trigger the event.
    val region: String = "",
) : EventEntry

@EntryListener(ExitRegionEventEntry::class)
fun onExitRegions(event: RegionsExitEvent, query: Query<ExitRegionEventEntry>) {
    val player = server.getPlayer(event.player.uniqueId) ?: return
    query findWhere { it.region in event } triggerAllFor player
}

