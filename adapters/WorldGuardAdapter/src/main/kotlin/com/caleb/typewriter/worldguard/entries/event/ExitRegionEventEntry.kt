package com.caleb.typewriter.worldguard.entries.event

import com.caleb.typewriter.worldguard.RegionsExitEvent
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("on_exit_region", "When a player exits a WorldGuard region", Colors.YELLOW, Icons.SQUARE_XMARK)
class ExitRegionEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Help("The region to check for.")
	val region: String = "",
) : EventEntry

@EntryListener(ExitRegionEventEntry::class)
fun onExitRegions(event: RegionsExitEvent, query: Query<ExitRegionEventEntry>) {
	val player = server.getPlayer(event.player.uniqueId) ?: return
	query findWhere { it.region in event } triggerAllFor player
}

