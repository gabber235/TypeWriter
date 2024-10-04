package com.typewritermc.entity.entries.activity

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entity.*
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.entry.roadnetwork.RoadNetworkManager
import com.typewritermc.engine.paper.entry.roadnetwork.gps.PointToPointGPS
import com.typewritermc.engine.paper.utils.distanceSqrt
import com.typewritermc.engine.paper.utils.toBukkitLocation
import org.koin.core.component.KoinComponent
import org.koin.java.KoinJavaComponent

@Entry("random_patrol_activity", "Moving around a set of locations randomly", Colors.GREEN, "fa6-solid:shuffle")
/**
 * The `RandomPatrolActivity` is an activity that makes the entity move randomly around a set of locations.
 * The entity will move to each location in a random order.
 *
 * ## How could this be used?
 * This could be used to make guards patrol randomly within a set of locations.
 */
class RandomPatrolActivityEntry(
    override val id: String = "",
    override val name: String = "",
    override val roadNetwork: Ref<RoadNetworkEntry> = emptyRef(),
    override val nodes: List<RoadNodeId> = emptyList(),
) : GenericEntityActivityEntry, RoadNodeCollectionEntry {
    override fun create(
        context: ActivityContext,
        currentLocation: PositionProperty
    ): EntityActivity<ActivityContext> {
        if (nodes.isEmpty()) return IdleActivity.create(context, currentLocation)
        val childActivity = RandomPatrolActivity(roadNetwork, nodes, currentLocation)
        return RandomPatrolActivityWrapper(childActivity)
    }
}

private class RandomPatrolActivityWrapper(
    private val childActivity: RandomPatrolActivity
) : EntityActivity<ActivityContext> {

    override fun initialize(context: ActivityContext) {
        childActivity.initialize(context)
    }

    override fun tick(context: ActivityContext): TickResult {
        return childActivity.tick(context)
    }

    override fun dispose(context: ActivityContext) {
        childActivity.dispose(context)
    }

    override val currentPosition: PositionProperty
        get() = childActivity.currentPosition

    override val currentProperties: List<EntityProperty>
        get() = childActivity.currentProperties
}

private class RandomPatrolActivity(
    private val roadNetwork: Ref<RoadNetworkEntry>,
    private val nodes: List<RoadNodeId>,
    private val startLocation: PositionProperty,
) : EntityActivity<ActivityContext>, KoinComponent {
    private var currentLocationIndex = 0
    private var activity: EntityActivity<in ActivityContext>? = null

    fun refreshActivity(context: ActivityContext, network: RoadNetwork) {
        val randomNodeId = nodes.randomOrNull()
            ?: throw IllegalStateException("Could not find any node in the nodes list for the random patrol activity.")
        val targetNode = network.nodes.find { it.id == randomNodeId }
            ?: throw IllegalStateException("Could not find any node in the nodes list for the random patrol activity.")

        activity?.dispose(context)
        activity = NavigationActivity(PointToPointGPS(
            roadNetwork,
            { currentPosition.toBukkitLocation() }) {
            targetNode.location
        }, currentPosition
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
            .minByOrNull { it.location.distanceSqrt(startLocation.toBukkitLocation()) ?: Double.MAX_VALUE }
            ?: throw IllegalStateException("Could not find any node in the nodes list for the random patrol activity.")

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
            refreshActivity(context, network) // Select a random node
        }

        return TickResult.CONSUMED
    }

    override fun dispose(context: ActivityContext) {
        activity?.dispose(context)
        activity = null
    }

    override val currentPosition: PositionProperty
        get() = activity?.currentPosition ?: startLocation

    override val currentProperties: List<EntityProperty>
        get() = activity?.currentProperties?.filter { it !is PositionProperty } ?: listOf()
}
