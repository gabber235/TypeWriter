package com.caleb.typewriter.worldguard.entries.audience

import com.caleb.typewriter.worldguard.RegionsEnterEvent
import com.caleb.typewriter.worldguard.RegionsExitEvent
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

@Entry(
    "region_audience",
    "Filter players based on if they are in a region",
    Colors.MEDIUM_SEA_GREEN,
    "gis:location-man"
)
/**
 * The `Region Audience` is used to filter players based on if they are in a specific WorldGuard region.
 *
 * ## How could this be used?
 * To show players information when they enter a specific region.
 * For example, when a player enters a town, you could show them information about the town.
 */
class RegionAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    override val children: List<Ref<AudienceEntry>> = emptyList(),
    @Help("The region to filter players based on")
    val region: String = "",
) : AudienceFilterEntry {
    override fun display(): AudienceFilter {
        return RegionAudienceFilter(region, children.into())
    }
}

class RegionAudienceFilter(
    private val region: String,
    children: List<AudienceDisplay>,
) : AudienceFilter(children) {

    @EventHandler
    fun onRegionEnter(event: RegionsEnterEvent) {
        if (event.player.uniqueId !in this) return
        if (event.regions.none { it.id == region }) return
        val player = server.getPlayer(event.player.uniqueId) ?: return
        player.updateFilter(true)
    }

    @EventHandler
    fun onRegionLeave(event: RegionsExitEvent) {
        if (event.player.uniqueId !in this) return
        if (event.regions.none { it.id == region }) return
        val player = server.getPlayer(event.player.uniqueId) ?: return
        player.updateFilter(false)
    }

    override fun filter(player: Player): Boolean {
        val regionContainer = WorldGuard.getInstance().platform.regionContainer
        val regionManager = regionContainer.get(BukkitAdapter.adapt(player.world))
        val regions = regionManager?.getApplicableRegions(BukkitAdapter.asBlockVector(player.location))
            ?: return false

        return regions.regions.any { it.id == region }
    }
}