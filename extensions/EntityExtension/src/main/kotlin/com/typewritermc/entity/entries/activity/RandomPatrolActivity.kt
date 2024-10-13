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
        return PatrolActivity(roadNetwork, nodes, currentLocation) { nodes, index ->
            var nextIndex: Int
            do {
                nextIndex = nodes.indices.random()
            } while (nextIndex == index)
            nextIndex
        }
    }
}