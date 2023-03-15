package com.caleb.typewriter.griefdefender.entries.fact

import com.griefdefender.api.GriefDefender

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("in_gd_region_fact", "If the player is in a GriefDefender region", Colors.PURPLE, Icons.ROAD_BARRIER)
data class InRegionFact(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	@Help("The UUID of the region which the player must be in")
	val region: String = "",
) : ReadableFactEntry {
	override fun read(playerId: UUID): Fact {
		val player = server.getPlayer(playerId) ?: return Fact(id, 0)

		val claim = GriefDefender.getCore().getClaimAt(player.location)
			?: return Fact(id, 0)

		val value = if (!claim.isWilderness && claim.uniqueId.toString() == region ) 1 else 0
		return Fact(id, value)
	}
}