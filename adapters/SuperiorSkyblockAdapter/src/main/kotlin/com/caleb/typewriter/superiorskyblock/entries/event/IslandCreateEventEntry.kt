package com.caleb.typewriter.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("on_create_island", "[SuperiorSkyblock] When a player creates an Island", Colors.YELLOW, Icons.GLOBE)
class IslandCreateEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(IslandCreateEventEntry::class)
fun onInvite(event: IslandCreateEvent, query: Query<IslandCreateEventEntry>) {

    val player: Player = event.player.asPlayer() ?: return

    query.find() triggerAllFor player
}