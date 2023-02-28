package com.caleb.typewriter.worldguard.entries.event

import com.caleb.typewriter.worldguard.RegionsEnterEvent
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("on_enter_region", "When a player enters a WorldGuard region", Colors.YELLOW, Icons.SQUARE_CHECK)
class EnterRegionEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Help("The region to check for.")
	val region: String = "",
) : EventEntry

@EntryListener(EnterRegionEventEntry::class)
fun onEnterRegions(event: RegionsEnterEvent, query: Query<EnterRegionEventEntry>) {
	val player = server.getPlayer(event.player.uniqueId) ?: return
	query findWhere { it.region in event } triggerAllFor player
}

