package com.caleb.typewriter.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandJoinEvent
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.EntryListener
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.triggerAllFor
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("on_island_join", "When a player joins a Skyblock island", Colors.YELLOW, Icons.ENVELOPE_OPEN)
/**
 * The `Island Join Event` is fired when a player joins an island.
 *
 * ## How could this be used?
 *
 * This event could be used for a "better together" system, where players can join islands and get rewards for doing so.
 */
class IslandJoinEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList()
) : EventEntry

@EntryListener(IslandJoinEventEntry::class)
fun onJoinIsland(event: IslandJoinEvent, query: Query<IslandJoinEventEntry>) {
    val sPlayer: SuperiorPlayer = event.player ?: return
    val player: Player = sPlayer.asPlayer() ?: return

    if (sPlayer.island == null) return

    query.find() triggerAllFor player
}

