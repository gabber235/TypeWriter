package com.caleb.typewriter.rpgregions.entries.fact

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import net.islandearth.rpgregions.api.RPGRegionsAPI
import org.bukkit.util.Vector
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
			?: return Fact(id, 0)

		if (player.getLocation().world !== region.get().location.world
			|| region.get().boundingBox.isNullOrEmpty()) return Fact(id, 0)

		val boundingBox = region.get().boundingBox
		val minVector = Vector.getMinimum(boundingBox?.get(0)!!.toVector(), boundingBox[1]!!.toVector())
		val maxVector = Vector.getMaximum(region.get().boundingBox?.get(0)!!.toVector(), boundingBox[1]!!.toVector())

		val value = if (player.location.toVector().isInAABB(minVector, maxVector)) 1 else 0
		return Fact(id, value)
	}
}