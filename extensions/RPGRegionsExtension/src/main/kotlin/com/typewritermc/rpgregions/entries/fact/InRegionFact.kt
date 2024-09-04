package com.typewritermc.rpgregions.entries.fact

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import net.islandearth.rpgregions.api.RPGRegionsAPI
import org.bukkit.entity.Player

@Entry("in_rpg_region_fact", "If the player is in a RPGRegions region", Colors.PURPLE, "fa6-solid:road-barrier")
/**
 * A [fact](/docs/creating-stories/facts) that checks if the player is in a specific region. The value will be `0` if the player is not in the region, and `1` if the player is in the region.
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
    override val group: Ref<GroupEntry> = emptyRef(),
    @Help("Make sure that this is the region ID, not the region's display name.")
    val region: String = "",
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val region = RPGRegionsAPI.getAPI().managers.regionsCache.getConfiguredRegion(region)
        if (!region.isPresent) return FactData(0)

        val standingRegion = RPGRegionsAPI.getAPI().managers.integrationManager
            .getPrioritisedRegion(player.location)
        if (!standingRegion.isPresent) return FactData(0)

        val value = if (standingRegion.get() == region.get()) 1 else 0
        return FactData(value)
    }
}