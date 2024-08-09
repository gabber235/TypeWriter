package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkManager
import me.gabber235.typewriter.entry.roadnetwork.gps.PointToPointGPS
import me.gabber235.typewriter.utils.distanceSqrt
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent


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
) : GenericEntityActivityEntry, RoadNodeCollectionEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: LocationProperty
    ): EntityActivity<ActivityContext> {
        if (nodes.isEmpty()) return IdleActivity.create(context, currentLocation)
        return PatrolActivity(roadNetwork, nodes, currentLocation)
    }
}

private class PatrolActivity(
    private val roadNetwork: Ref<RoadNetworkEntry>,
    private val nodes: List<RoadNodeId>,
    private val startLocation: LocationProperty,
) : EntityActivity<ActivityContext>, KoinComponent {
    private var currentLocationIndex = 0
    private var activity: EntityActivity<in ActivityContext>? = null

    fun refreshActivity(context: ActivityContext, network: RoadNetwork) {
        val targetNodeId = nodes.getOrNull(currentLocationIndex)
            ?: throw IllegalStateException("Could not find any node in the nodes list for the patrol activity.")
        val targetNode = network.nodes.find { it.id == targetNodeId }
            ?: throw IllegalStateException("Could not find any node in the nodes list for the patrol activity.")

        activity?.dispose(context)
        activity = NavigationActivity(PointToPointGPS(
            roadNetwork,
            { currentLocation.toLocation() }) {
            targetNode.location
        }, currentLocation
        )
        activity?.initialize(context)
    }

    override fun initialize(context: ActivityContext) = setup(context)

    private fun setup(context: ActivityContext) {
        val network =
            KoinJavaComponent.get<RoadNetworkManager>(RoadNetworkManager::class.java).getNetworkOrNull(roadNetwork)
                ?: return

        // Get the closest node to the start location
        val closestNode = network.nodes
            .filter { it.id in nodes }
            .minByOrNull { it.location.distanceSqrt(startLocation.toLocation()) ?: Double.MAX_VALUE }
            ?: throw IllegalStateException("Could not find any node in the nodes list for the patrol activity.")

        val index = nodes.indexOf(closestNode.id)
        currentLocationIndex = (index + 1) % nodes.size
        refreshActivity(context, network)
    }


    override fun tick(context: ActivityContext): TickResult {
        if (activity == null) {
            setup(context)
            return TickResult.CONSUMED
        }

        val network =
            KoinJavaComponent.get<RoadNetworkManager>(RoadNetworkManager::class.java).getNetworkOrNull(roadNetwork)
                ?: return TickResult.IGNORED

        val result = activity?.tick(context)
        if (result == TickResult.IGNORED) {
            currentLocationIndex = (currentLocationIndex + 1) % nodes.size
            refreshActivity(context, network)
        }

        return TickResult.CONSUMED
    }

    override fun dispose(context: ActivityContext) {
        activity?.dispose(context)
        activity = null
    }

    override val currentLocation: LocationProperty
        get() = activity?.currentLocation ?: startLocation
}
