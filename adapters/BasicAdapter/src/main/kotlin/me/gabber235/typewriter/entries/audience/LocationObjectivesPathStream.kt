package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.entry.entries.trackedShowingObjectives

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.roadnetwork.gps.MultiPathStreamDisplay
import me.gabber235.typewriter.entries.quest.LocationObjectiveEntry

@Entry(
    "location_objectives_path_stream",
    "A Path Stream to tracked Location Objectives",
    Colors.GREEN,
    "material-symbols:conversion-path"
)
/**
 * The `Location Objectives Path Stream` entry is a path stream that shows the path to each tracked location objective.
 * When the player has a location objective, and the quest for the objective is tracked, a path stream will be displayed.
 *
 * ## How could this be used?
 * This could be used to show a path to each location objective in a quest.
 */
class LocationObjectivesPathStream(
    override val id: String = "",
    override val name: String = "",
    val road: Ref<RoadNetworkEntry> = emptyRef(),
) : AudienceEntry {
    override fun display(): AudienceDisplay = MultiPathStreamDisplay(road, endLocations = { player ->
        player.trackedShowingObjectives().filterIsInstance<LocationObjectiveEntry>().map { it.targetLocation }.toList()
    })
}