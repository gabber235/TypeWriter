package com.typewritermc.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent
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

@Entry("on_island_disband", "When a player disbands an Island", Colors.YELLOW, "fa6-solid:globe")
/**
 * The `Island Disband Event` is triggered when an island is disbanded.
 *
 * ## How could this be used?
 *
 * This could be used to allow for some "re-birthing". So that the next time the players have bonuses or something.
 */
class IslandDisbandEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : EventEntry

@EntryListener(MissionCompleteEventEntry::class)
fun onDisbandIsland(event: IslandDisbandEvent, query: Query<MissionCompleteEventEntry>) {
    val player: Player = event.player.asPlayer() ?: return

    query.find().triggerAllFor(player, context())
}