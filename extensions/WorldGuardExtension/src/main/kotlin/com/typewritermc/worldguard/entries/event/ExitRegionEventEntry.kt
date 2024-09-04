package com.typewritermc.worldguard.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor
import com.typewritermc.worldguard.RegionsExitEvent
import lirand.api.extensions.server.server

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
    val region: String = "",
) : EventEntry

@EntryListener(ExitRegionEventEntry::class)
fun onExitRegions(event: RegionsExitEvent, query: Query<ExitRegionEventEntry>) {
    query findWhere { it.region in event } triggerAllFor event.player
}

