package com.typewritermc.worldguard.entries.fact

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import org.bukkit.entity.Player

@Entry("in_region_fact", "If the player is in a WorldGuard region", Colors.PURPLE, "fa6-solid:road-barrier")
/**
 * A [fact](/docs/creating-stories/facts) that checks if the player is in a specific region. The value will be `0` if the player is not in the region, and `1` if the player is in the region.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 *
 * This fact could be used to make a quest only available in a specific region.
 */
class InRegionFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    val region: String = "",
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val regionContainer = WorldGuard.getInstance().platform.regionContainer
        val regionManager = regionContainer.get(BukkitAdapter.adapt(player.world))
        val regions = regionManager?.getApplicableRegions(BukkitAdapter.asBlockVector(player.location))
            ?: return FactData(0)

        val value = if (regions.regions.any { it.id == region }) 1 else 0
        return FactData(value)
    }
}