package com.caleb.typewriter.worldguard.entries.audience

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceId
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("region_audience", "All players grouped by WorldGuard regions", Colors.MYRTLE_GREEN, Icons.OBJECT_GROUP)
/**
 * The `WorldGuardRegionAudience` is an audience that includes all the players in a specific WorldGuard region.
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
        val regionContainer = WorldGuard.getInstance().platform.regionContainer
        val regionManager = regionContainer.get(BukkitAdapter.adapt(player.world))
        val regions =
            regionManager?.getApplicableRegions(BukkitAdapter.asBlockVector(player.location))?.regions?.map { it.id }

        regions?.forEach { region ->
            if (this.regions.contains(region)) {
                return AudienceId(region)
            }
        }

        return null
    }
}