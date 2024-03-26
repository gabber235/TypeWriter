package me.gabber235.typewriter.entry.roadnetwork

import me.gabber235.typewriter.entry.entries.RoadNode
import org.patheloper.api.pathing.strategy.PathValidationContext
import org.patheloper.api.pathing.strategy.strategies.WalkablePathfinderStrategy

class NodeAvoidPathfindingStrategy(
    height: Int = 2,
    private val nodeAvoidance: List<RoadNode> = emptyList()
) : WalkablePathfinderStrategy(height) {
    private var hasComeAcrossNode = false
    override fun isValid(pathValidationContext: PathValidationContext): Boolean {
        if (hasComeAcrossNode) return false
        if (!super.isValid(pathValidationContext)) return false

        val center = pathValidationContext.position.mid()
        val inRange = nodeAvoidance.any { center.distanceSquared(it.location.toPathPosition()) < it.radius * it.radius }
        if (inRange) {
            hasComeAcrossNode = true
        }
        return !inRange
    }
}