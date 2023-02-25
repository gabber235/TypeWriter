package com.caleb.typewriter.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import java.util.*

@Entry("on_disband_island", "[SuperiorSkyblock] When a player disbands an Island", Colors.YELLOW, Icons.GLOBE)
class IslandDisbandEventEntry (
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(MissionCompleteEventEntry::class)
fun onDisbandIsland(event: IslandDisbandEvent, query: Query<MissionCompleteEventEntry>) {

	var player: Player = event.player.asPlayer() ?: return

	query.find() triggerAllFor player
}