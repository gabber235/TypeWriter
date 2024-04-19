package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.entries.RoadNodeCollectionEntry
import me.gabber235.typewriter.entry.entries.RoadNodeId
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkManager
import me.gabber235.typewriter.entry.roadnetwork.gps.PointToPointGPS
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent
import java.util.*


@Entry("patrol_activity", "Moving around a set of locations", Colors.BLUE, "fa6-solid:route")
/**
 * The `PatrolActivity` is an activity that makes the entity move around a set of locations.
 * The entity will move to each location in the set in order.
 * Once the entity reaches the last location, it will start back at the first location.
 *
 * ## How could this be used?
 * This could be used to make guards patrol around a set of locations.
 */
class PatrolActivityEntry(
    override val id: String = "",
    override val name: String = "",
    override val roadNetwork: Ref<RoadNetworkEntry> = emptyRef(),
    override val nodes: List<RoadNodeId> = emptyList(),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityActivityEntry, RoadNodeCollectionEntry {
    override fun create(context: TaskContext): EntityActivity = PatrolActivity(roadNetwork, nodes)
}

private class PatrolActivity(
    private val roadNetwork: Ref<RoadNetworkEntry>,
    private val nodes: List<RoadNodeId>,
) : EntityActivity, KoinComponent {
    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean = nodes.isNotEmpty()

    private var currentLocationIndex = 0

    override fun currentTask(context: TaskContext, currentLocation: LocationProperty): EntityTask {
        val network = KoinJavaComponent.get<RoadNetworkManager>(RoadNetworkManager::class.java).getNetworkOrNull(roadNetwork)
            ?: return IdleTask(currentLocation)

        val nodeId = nodes.getOrNull(currentLocationIndex) ?: return IdleTask(currentLocation)
        val currentTargetNode = network.nodes.find { it.id == nodeId }
            ?: return IdleTask(currentLocation)

        // Only move to the next location if the entity is close to the current location
        if (currentLocation.toLocation().distanceSquared(currentTargetNode.location) < 1.0) {
            currentLocationIndex = (currentLocationIndex + 1) % nodes.size
        }

        val gps = PointToPointGPS(
            roadNetwork,
            { currentLocation.toLocation() }) {
            it.nodes.find { node -> node.id == nodeId }?.location ?: currentLocation.toLocation()
        }
        return NavigationActivityTask(gps, currentLocation)
    }
}
