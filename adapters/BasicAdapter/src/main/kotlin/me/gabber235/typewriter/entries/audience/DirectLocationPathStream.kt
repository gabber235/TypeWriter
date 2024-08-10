package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.roadnetwork.gps.PathStreamDisplay
import org.bukkit.Location

@Entry(
    "direct_location_path_stream",
    "A Path Stream to a Direct Location",
    Colors.GREEN,
    "material-symbols:conversion-path"
)
/**
 * The `Direct Location Path Stream` entry is a path stream that shows the path to a specific location.
 * When the player has this entry, a path stream will be displayed to the specified location.
 *
 * ## How could this be used?
 * This could be used to show a path to a specific location in the world.
 */
class DirectLocationPathStream(
    override val id: String = "",
    override val name: String = "",
    val road: Ref<RoadNetworkEntry> = emptyRef(),
    val targetLocation: Location = Location(null, 0.0, 0.0, 0.0),
) : AudienceEntry {
    override fun display(): AudienceDisplay = PathStreamDisplay(road, endLocation = { targetLocation })
}