package me.gabber235.typewriter.entries.activity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkManager
import me.gabber235.typewriter.entry.roadnetwork.gps.PointToPointGPS
import org.koin.java.KoinJavaComponent

@Entry("path_activity", "Moving along a predefined path", Colors.BLUE, "material-symbols:conversion-path")
/**
 * The `Path Activity` is an activity that moves along a predefined path.
 * The entity will move to each location in the set in order.
 * Once the entity reaches the last location, it will do the idle activity.
 *
 * ## How could this be used?
 * This could be used to have a tour guide that moves along the post-important paths.
 */
class PathActivityEntry(
    override val id: String = "",
    override val name: String = "",
    override val roadNetwork: Ref<RoadNetworkEntry> = emptyRef(),
    override val nodes: List<RoadNodeId> = emptyList(),
    @Help("The activity that will be used when the entity is at the final location.")
    val idleActivity: Ref<out EntityActivityEntry> = emptyRef(),
): GenericEntityActivityEntry, RoadNodeCollectionEntry {
    override fun create(context: ActivityContext, currentLocation: LocationProperty): EntityActivity<ActivityContext> {
        if (nodes.isEmpty()) return IdleActivity.create(context, currentLocation)
        return PathActivity(roadNetwork, nodes, currentLocation, idleActivity)
    }
}

private class PathActivity(
    private val roadNetwork: Ref<RoadNetworkEntry>,
    private val nodes: List<RoadNodeId>,
    private val startLocation: LocationProperty,
    private val idleActivity: Ref<out EntityActivityEntry>,
) : GenericEntityActivity {
    private var currentLocationIndex = 0
    private var activity: EntityActivity<in ActivityContext>? = null

    fun refreshActivity(context: ActivityContext, network: RoadNetwork) {
        if (nodes.size <= currentLocationIndex) {
            activity?.dispose(context)
            activity = idleActivity.get()?.create(context, currentLocation)
            activity?.initialize(context)
            return
        }

        val targetNodeId = nodes.getOrNull(currentLocationIndex)
            ?: throw IllegalStateException("Could not find any node in the nodes list for the path activity.")
        val targetNode = network.nodes.find { it.id == targetNodeId }
            ?: throw IllegalStateException("Could not find any node in the nodes list for the path activity.")

        activity?.dispose(context)
        activity = NavigationActivity(
            PointToPointGPS(
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
            .minByOrNull { it.location.distanceSquared(startLocation.toLocation()) }
            ?: throw IllegalStateException("Could not find any node in the nodes list for the patrol activity.")

        val index = nodes.indexOf(closestNode.id)
        currentLocationIndex = (index + 1)
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
            currentLocationIndex = (currentLocationIndex + 1)
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