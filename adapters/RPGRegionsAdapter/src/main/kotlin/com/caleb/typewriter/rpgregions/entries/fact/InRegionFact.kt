package com.caleb.typewriter.rpgregions.entries.fact

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import net.islandearth.rpgregions.api.RPGRegionsAPI
import java.util.*

@Entry("in_RPGRegion_fact", "If the player is in a RPGRegions region", Colors.PURPLE, Icons.ROAD_BARRIER)
data class InRegionFact(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	@Help("The name of the region which the player must be in")
	val region: String = "",
) : ReadableFactEntry {
	override fun read(playerId: UUID): Fact {
		val player = server.getPlayer(playerId) ?: return Fact(id, 0)

		val region = RPGRegionsAPI.getAPI().managers.regionsCache.getConfiguredRegion(region)
		if (!region.isPresent) return Fact(id, 0)

		val standingRegion = RPGRegionsAPI.getAPI().managers.integrationManager
			.getPrioritisedRegion(player.location)
		if (!standingRegion.isPresent) return Fact(id, 0)

		val value = if (standingRegion.get() == region.get()) 1 else 0
		return Fact(id, value)
	}
}