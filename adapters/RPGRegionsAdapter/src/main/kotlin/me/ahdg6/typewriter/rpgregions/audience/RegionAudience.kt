package me.ahdg6.typewriter.rpgregions.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceId
import me.gabber235.typewriter.utils.Icons
import net.islandearth.rpgregions.api.RPGRegionsAPI
import org.bukkit.entity.Player
import kotlin.jvm.optionals.getOrNull

@Entry("rpg_region_audience", "All players grouped by RPGRegions regions", Colors.MYRTLE_GREEN, Icons.OBJECT_GROUP)
/**
 * The `Region Audience` is an audience that includes all the players in a specific RPGRegions region.
 * Only the given region will be considered for the audience.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to a region, like the state of a boss fight.
 * It could also be used to send a title to all the players in a region.
 */
class RegionAudience(
    override val id: String = "",
    override val name: String = "",
    @Help("The names of regions to consider for the audience")
    val regions: List<String> = emptyList(),
) : AudienceEntry {
    override fun audienceId(player: Player): AudienceId? {
        val standingRegion = RPGRegionsAPI.getAPI().managers.integrationManager
            .getPrioritisedRegion(player.location).getOrNull() ?: return null

        for (region in regions) {
            val regionData =
                RPGRegionsAPI.getAPI().managers.regionsCache.getConfiguredRegion(region).getOrNull() ?: continue
            if (regionData == standingRegion) return AudienceId(regionData.id)
        }

        return null
    }
}