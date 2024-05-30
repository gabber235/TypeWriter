package me.ahdg6.typewriter.rpgregions.entries.event

import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import net.islandearth.rpgregions.api.events.RegionDiscoverEvent

@Entry(
    "on_discover_rpg_region",
    "When a player discovers an RPGRegions region",
    Colors.YELLOW,
    "fa-solid:location-arrow"
)
/**
 * The `Discover Region Event` is triggered when a player discovers a region.
 *
 * ## How could this be used?
 *
 * This event could be used to trigger a message to the player when they discover a region, like a welcome.
 * Or when they discover a region, it could trigger a quest to start and start a dialogue or cinematic.
 */
class DiscoverRegionEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The region to check for.")
    // The region to check for. Make sure that this is the region ID, not the region's display name.
    val region: String = "",
) : EventEntry

@EntryListener(DiscoverRegionEventEntry::class)
fun onDiscoverRegions(event: RegionDiscoverEvent, query: Query<DiscoverRegionEventEntry>) {
    val player = server.getPlayer(event.player.uniqueId) ?: return
    query findWhere { it.region == event.region } triggerAllFor player
}