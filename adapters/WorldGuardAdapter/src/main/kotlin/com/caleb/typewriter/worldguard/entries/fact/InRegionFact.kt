package com.caleb.typewriter.worldguard.entries.fact

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("in_region_fact", "If the player is in a WorldGuard region", Colors.PURPLE, Icons.ROAD_BARRIER)
data class InRegionFact(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	@Help("The name of the region which the player must be in")
	val region: String = "",
) : ReadableFactEntry {
	override fun read(playerId: UUID): Fact {
		val player = server.getPlayer(playerId) ?: return Fact(id, 0)
		val regionContainer = WorldGuard.getInstance().platform.regionContainer
		val regionManager = regionContainer.get(BukkitAdapter.adapt(player.world))
		val regions = regionManager?.getApplicableRegions(BukkitAdapter.asBlockVector(player.location))
			?: return Fact(id, 0)

		val value = if (regions.regions.any { it.id == region }) 1 else 0
		return Fact(id, value)
	}
}