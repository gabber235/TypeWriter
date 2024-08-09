package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entity.ActivityEntityDisplay
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.EntityInstanceEntry
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.roadnetwork.gps.PathStreamDisplay
import org.koin.java.KoinJavaComponent

@Entry(
    "direct_entity_instance_path_stream",
    "A Path Stream to a Direct Entity Instance",
    Colors.GREEN,
    "material-symbols:conversion-path"
)
/**
 * The `Direct Entity Instance Path Stream` entry is a path stream that shows the path to a specific entity instance.
 * When the player has this entry, a path stream will be displayed to the specified entity instance.
 *
 * ## How could this be used?
 * This could be used to show a path to a specific entity instance in the world.
 */
class DirectEntityInstancePathStream(
    override val id: String = "",
    override val name: String = "",
    val road: Ref<RoadNetworkEntry> = emptyRef(),
    val target: Ref<EntityInstanceEntry> = emptyRef(),
) : AudienceEntry {
    override fun display(): AudienceDisplay {
        val manager = KoinJavaComponent.get<AudienceManager>(AudienceManager::class.java)
        val entityDisplay = manager[target] as? ActivityEntityDisplay
        return PathStreamDisplay(road, endLocation = { entityDisplay?.location(it.uniqueId) ?: it.location })
    }
}