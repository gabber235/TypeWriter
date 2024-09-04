package com.typewritermc.worldguard.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor
import com.typewritermc.worldguard.RegionsEnterEvent
import lirand.api.extensions.server.server

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
    val region: String = "",
) : EventEntry

@EntryListener(EnterRegionEventEntry::class)
fun onEnterRegions(event: RegionsEnterEvent, query: Query<EnterRegionEventEntry>) {
    query findWhere { it.region in event } triggerAllFor event.player
}

