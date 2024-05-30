package me.gabber235.typewriter.entries.quest

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entity.ActivityEntityDisplay
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.roadnetwork.gps.MultiPathStreamDisplay
import org.koin.java.KoinJavaComponent

@Entry(
    "interact_entity_objectives_path_stream",
    "A Path Stream to Interact Entity Objectives",
    Colors.GREEN,
    "material-symbols:conversion-path"
)
/**
 * The `Interact Entity Objectives Path Stream` entry is a path stream that shows the path to each interact entity objective.
 * When the player has an interact entity objective, and the quest for the objective is tracked, a path stream will be displayed.
 *
 * The `Ignore Instances` field is a list of entity instances that should be ignored when showing the path.
 *
 * ## How could this be used?
 * This could be used to show a path to each interacting entity objective in a quest.
 */
class InteractEntityObjectivesPathStream(
    override val id: String = "",
    override val name: String = "",
    val road: Ref<RoadNetworkEntry> = emptyRef(),
    val ignoreInstances: List<Ref<EntityInstanceEntry>> = emptyList(),
) : AudienceEntry {
    override fun display(): AudienceDisplay = MultiPathStreamDisplay(road, endLocations = { player ->
        val definitions =
            player.trackedShowingObjectives().filterIsInstance<InteractEntityObjective>().map { it.entity }
        val manager = KoinJavaComponent.get<AudienceManager>(AudienceManager::class.java)
        Query.findWhere<EntityInstanceEntry> { it.ref() !in ignoreInstances && it.definition in definitions }
            .mapNotNull { manager[it.ref()] }
            .filterIsInstance<ActivityEntityDisplay>()
            .filter { it.canView(player.uniqueId) }
            .mapNotNull { it.location(player.uniqueId) }
            .toList()
    })
}