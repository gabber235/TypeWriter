package com.caleb.typewriter.rpgregions.entries.event

import net.islandearth.rpgregions.api.events.RegionDiscoverEvent
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("on_discover_rpg_region", "When a player discovers an RPGRegions region", Colors.YELLOW, Icons.LOCATION_ARROW)
class DiscoverRegionEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Help("The region to check for.")
	val region: String = "",
) : EventEntry

@EntryListener(DiscoverRegionEventEntry::class)
fun onDiscoverRegions(event: RegionDiscoverEvent, query: Query<DiscoverRegionEventEntry>) {
	val player = server.getPlayer(event.player.uniqueId) ?: return
	query findWhere { it.region == event.region } triggerAllFor player
}