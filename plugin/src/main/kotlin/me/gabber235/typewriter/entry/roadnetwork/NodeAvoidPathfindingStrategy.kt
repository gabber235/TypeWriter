package me.gabber235.typewriter.entry.roadnetwork

import me.gabber235.typewriter.entry.entries.RoadNode
import me.gabber235.typewriter.entry.roadnetwork.content.toPathPosition
import org.patheloper.api.pathing.strategy.PathValidationContext
import org.patheloper.api.pathing.strategy.PathfinderStrategy

class NodeAvoidPathfindingStrategy(
    private val strategy: PathfinderStrategy,
    private val nodeAvoidance: List<RoadNode> = emptyList(),
    private val permanentLock: Boolean = false
) : PathfinderStrategy {
    private var hasComeAcrossNode = false
    override fun isValid(pathValidationContext: PathValidationContext): Boolean {
        if (hasComeAcrossNode) return false
        if (!strategy.isValid(pathValidationContext)) return false

        val center = pathValidationContext.position.mid()
        val inRange = nodeAvoidance.any {
            val pos = it.location.toPathPosition().mid()
            center.distanceSquared(pos) < it.radius * it.radius
        }
        if (inRange) {
            hasComeAcrossNode = permanentLock
        }
        return !inRange
    }

    override fun cleanup() {
        super.cleanup()
        hasComeAcrossNode = false
        strategy.cleanup()
    }
}