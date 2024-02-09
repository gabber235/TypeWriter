package com.caleb.typewriter.worldguard.entries.fact

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.FactData
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("in_region_fact", "If the player is in a WorldGuard region", Colors.PURPLE, Icons.ROAD_BARRIER)
/**
 * A [fact](/docs/facts) that checks if the player is in a specific region. The value will be `0` if the player is not in the region, and `1` if the player is in the region.
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
    @Help("The name of the region which the player must be in.")
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