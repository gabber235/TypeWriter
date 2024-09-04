package com.typewritermc.basic.entries.audience

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.basic.entries.quest.LocationObjectiveEntry
import com.typewritermc.engine.paper.entry.entries.AudienceDisplay
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import com.typewritermc.engine.paper.entry.entries.RoadNetworkEntry
import com.typewritermc.engine.paper.entry.entries.trackedShowingObjectives
import com.typewritermc.engine.paper.entry.roadnetwork.gps.MultiPathStreamDisplay
import com.typewritermc.engine.paper.utils.toBukkitLocation

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
        player.trackedShowingObjectives().filterIsInstance<LocationObjectiveEntry>()
            .map { it.targetLocation.toBukkitLocation() }.toList()
    })
}