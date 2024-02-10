package me.ahdg6.typewriter.rpgregions.group

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.GroupId
import net.islandearth.rpgregions.api.RPGRegionsAPI
import org.bukkit.entity.Player
import kotlin.jvm.optionals.getOrNull

@Entry("rpg_region_group", "All players grouped by RPGRegions regions", Colors.MYRTLE_GREEN, "fa-solid:object-group")
/**
 * The `Region Group` is a group that includes all the players in a specific RPGRegions region.
 * Only the given region will be considered for the group.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to a region, like the state of a boss fight.
 * It could also be used to send a title to all the players in the region.
 */
class RegionGroup(
    override val id: String = "",
    override val name: String = "",
    @Help("The names of regions to consider for the group")
    val regions: List<String> = emptyList(),
) : GroupEntry {
    override fun groupId(player: Player): GroupId? {
        val standingRegion = RPGRegionsAPI.getAPI().managers.integrationManager
            .getPrioritisedRegion(player.location).getOrNull() ?: return null

        for (region in regions) {
            val regionData =
                RPGRegionsAPI.getAPI().managers.regionsCache.getConfiguredRegion(region).getOrNull() ?: continue
            if (regionData == standingRegion) return GroupId(regionData.id)
        }

        return null
    }
}