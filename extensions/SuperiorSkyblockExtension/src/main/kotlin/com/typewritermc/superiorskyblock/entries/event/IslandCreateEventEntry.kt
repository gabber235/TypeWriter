package com.typewritermc.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor
import org.bukkit.entity.Player

@Entry("on_island_create", "When a player creates an Island", Colors.YELLOW, "fa6-solid:globe")
/**
 * The `Island Create Event` is triggered when an island is created.
 *
 * ## How could this be used?
 *
 * This event could be used to give the player starting items when they create an island.
 */
class IslandCreateEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : EventEntry

@EntryListener(IslandCreateEventEntry::class)
fun onInvite(event: IslandCreateEvent, query: Query<IslandCreateEventEntry>) {
    val player: Player = event.player.asPlayer() ?: return

    query.find().triggerAllFor(player, context())
}