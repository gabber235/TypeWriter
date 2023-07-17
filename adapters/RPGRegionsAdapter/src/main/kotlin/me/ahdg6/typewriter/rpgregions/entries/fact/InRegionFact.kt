package me.ahdg6.typewriter.rpgregions.entries.fact

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import net.islandearth.rpgregions.api.RPGRegionsAPI
import java.util.*

@Entry("in_rpg_region_fact", "If the player is in a RPGRegions region", Colors.PURPLE, Icons.ROAD_BARRIER)
/**
 * A [fact](/docs/facts) that checks if the player is in a specific region. The value will be `0` if the player is not in the region, and `1` if the player is in the region.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 *
 * This fact could be used to make a quest only available in a specific region, or could even be used as a condition for player abilities that only work in specific areas!
 */
class InRegionFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    @Help("The name of the region which the player must be in")
    // The name of the region which the player must be in. Make sure that this is the region ID, not the region's display name.
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