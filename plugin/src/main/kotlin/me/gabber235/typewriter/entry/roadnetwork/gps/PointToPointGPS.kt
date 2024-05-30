package me.gabber235.typewriter.entry.roadnetwork.gps

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.await
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.roadnetwork.NodeAvoidPathfindingStrategy
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkManager
import me.gabber235.typewriter.entry.roadnetwork.content.toPathPosition
import me.gabber235.typewriter.utils.ComputedMap
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import me.gabber235.typewriter.utils.distanceSqrt
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok
import org.bukkit.Location
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.patheloper.api.pathing.configuration.PathingRuleSet
import org.patheloper.api.pathing.strategy.strategies.WalkablePathfinderStrategy
import org.patheloper.mapping.PatheticMapper
import java.util.*
import kotlin.collections.set

class PointToPointGPS(
    private val network: Ref<RoadNetworkEntry>,
    private val startFetcher: suspend (RoadNetwork) -> Location,
    private val endFetcher: suspend (RoadNetwork) -> Location,
) : GPS, KoinComponent {
    private val roadNetworkManager: RoadNetworkManager by inject()
    private var previousStart: Pair<RoadNode, List<RoadEdge>?>? = null
    private var previousEnd: Pair<RoadNode, List<RoadEdge>?>? = null
    private var previousPath: List<RoadEdge> = emptyList()

    override suspend fun findPath(): Result<List<GPSEdge>> = DISPATCHERS_ASYNC.switchContext {
        var network = roadNetworkManager.getNetwork(network)

        val start = startFetcher(network)
        val end = endFetcher(network)
        if ((start.distanceSqrt(end) ?: Double.MAX_VALUE) < 1) return@switchContext ok(emptyList())

        val endPair = getOrCreateNode(network.nodes, network.negativeNodes, end, previousEnd, -2, asEnd = true)
        previousEnd = endPair
        val startPair = getOrCreateNode(network.nodes + endPair.first, network.negativeNodes, start, previousStart, -1, asEnd = false)
        previousStart = startPair

        if (startPair.second?.isEmpty() == true || endPair.second?.isEmpty() == true) {
            return@switchContext failure("Could not find a path between $start and $end. The network is not connected.")
        }

        if (startPair.first == endPair.first) return@switchContext ok(emptyList())

        if (startPair.second != null || endPair.second != null) {
            network = constructNewNetwork(network, startPair, endPair)
        }

        val nodes = ComputedMap { id: RoadNodeId -> network.nodes.firstOrNull { it.id == id } }
        val path = findPath(nodes, network.edges, previousPath, startPair.first, endPair.first)
        if (path.isFailure) {
            return@switchContext path.exceptionOrNull()?.let { failure(it) }
                ?: failure("Could not find a path between $start and $end.")
        }
        previousPath = path.getOrThrow()
        ok(previousPath.map { edge ->
            GPSEdge(
                nodes[edge.start]!!.location,
                nodes[edge.end]!!.location,
                edge.weight
            )
        })
    }

    private fun findPath(
        nodes: ComputedMap<RoadNodeId, RoadNode>,
        edges: List<RoadEdge>,
        previous: List<RoadEdge>,
        start: RoadNode,
        end: RoadNode,
    ): Result<List<RoadEdge>> {
        val startEndDistance = start.location.distanceSqrt(end.location)
        val visited = mutableMapOf<RoadNodeId, VisitedNode>()
        val inspecting = PriorityQueue<InspectingNode>()
        inspecting += InspectingNode(
            start.id,
            null,
            0.0,
            distanceWeight(startEndDistance, start.location, end.location)
        )

        while (inspecting.isNotEmpty()) {
            val current = inspecting.poll()
            if (current.node == end.id) {
                return ok(findEdges(visited, start.id, current.edge!!))
            }

            visited[current.node] = VisitedNode(current.edge)

            if (current.node.id > 0) {
                val visitedEdge = previous.firstOrNull { it.start == current.node }
                if (visitedEdge != null) {
                    // We have come to a point where the previous path has already been
                    // We can do a reverse search from the end to the start and we will likely skip a lot of intermediate nodes.
                    val pathEdges = findEdges(visited, start.id, visitedEdge)
                    val edgeIndex = previous.indexOf(visitedEdge)
                    val path = pathEdges + previous.subList(edgeIndex + 1, previous.size)
                    return findPathFromEnd(nodes, edges, path, start, end)
                }
            }

            for (edge in edges) {
                if (edge.start != current.node) continue
                if (visited.containsKey(edge.end)) continue


                val next = nodes[edge.end]!!
                insertInspecting(end.location, startEndDistance, edge, next, current, inspecting)
            }
        }

        return failure("Could not find a path between $start and $end during search. The network is not connected.")
    }

    private fun findPathFromEnd(
        nodes: ComputedMap<RoadNodeId, RoadNode>,
        edges: List<RoadEdge>,
        path: List<RoadEdge>,
        start: RoadNode,
        end: RoadNode
    ): Result<List<RoadEdge>> {
        val startEndDistance = start.location.distanceSqrt(end.location)
        val visited = mutableMapOf<RoadNodeId, VisitedNode>()
        val inspecting = PriorityQueue<InspectingNode>()
        inspecting += InspectingNode(end.id, null, 0.0, distanceWeight(startEndDistance, end.location, start.location))

        while (inspecting.isNotEmpty()) {
            val current = inspecting.poll()
            if (current.node == start.id) {
                return ok(findEdgesReverse(visited, end.id, current.edge!!))
            }

            visited[current.node] = VisitedNode(current.edge)

            if (current.node.id > 0) {
                val visitedEdge = path.firstOrNull { it.end == current.node }
                if (visitedEdge != null) {
                    val pathEdges = findEdgesReverse(visited, end.id, visitedEdge)
                    val edgeIndex = path.indexOf(visitedEdge)
                    return ok(path.subList(0, edgeIndex) + pathEdges)
                }
            }

            for (edge in edges) {
                if (edge.end != current.node) continue
                if (visited.containsKey(edge.start)) continue

                val next = nodes[edge.start]!!
                insertInspecting(start.location, startEndDistance, edge, next, current, inspecting)
            }
        }

        return failure("Could not find a path between $start and $end during search. The network is not connected.")
    }

    private fun insertInspecting(
        targetLocation: Location,
        startEndDistance: Double?,
        edge: RoadEdge,
        next: RoadNode,
        current: InspectingNode,
        inspecting: PriorityQueue<InspectingNode>,
    ) {
        val weight = current.weight + edge.weight
        val oldInspecting = inspecting.firstOrNull { it.node == next.id }
        if (oldInspecting != null && oldInspecting.weight <= weight) return
        inspecting -= oldInspecting
        val inspectingNode =
            InspectingNode(next.id, edge, weight, distanceWeight(startEndDistance, next.location, targetLocation))
        inspecting += inspectingNode
    }

    private fun findEdges(
        visited: Map<RoadNodeId, VisitedNode>,
        startId: RoadNodeId,
        endEdge: RoadEdge
    ): List<RoadEdge> {
        val edges = mutableListOf<RoadEdge>()
        var current = endEdge
        while (current.start != startId) {
            edges += current
            current = visited[current.start]!!.edge!!
        }
        edges += current
        return edges.reversed()
    }

    private fun findEdgesReverse(
        visited: Map<RoadNodeId, VisitedNode>,
        startId: RoadNodeId,
        endEdge: RoadEdge
    ): List<RoadEdge> {
        val edges = mutableListOf<RoadEdge>()
        var current = endEdge
        while (current.end != startId) {
            edges += current
            current = visited[current.end]!!.edge!!
        }
        edges += current
        return edges
    }


    private suspend fun getOrCreateNode(
        nodes: List<RoadNode>,
        negativeNodes: List<RoadNode>,
        location: Location,
        previous: Pair<RoadNode, List<RoadEdge>?>?,
        id: Int,
        asEnd: Boolean,
    ): Pair<RoadNode, List<RoadEdge>?> {
        if (previous != null && (previous.first.location.distanceSqrt(location)
                ?: Double.MAX_VALUE) < previous.first.radius * previous.first.radius
        ) {
            return previous
        }

        val node = nodes.firstOrNull {
            (it.location.distanceSqrt(location) ?: Double.MAX_VALUE) < it.radius * it.radius
        }
        if (node != null) return node to null
        val newNode = RoadNode(RoadNodeId(id), location, 0.5)
        val additionalEdges = findAdditionalEdges(nodes, negativeNodes, newNode, asEnd)
        return newNode to additionalEdges
    }

    private suspend fun findAdditionalEdges(
        nodes: List<RoadNode>,
        negativeNodes: List<RoadNode>,
        node: RoadNode,
        asEnd: Boolean,
    ): List<RoadEdge> =
        coroutineScope {
            nodes
                .filter {
                    it != node && it.location.world == node.location.world && (it.location.distanceSqrt(node.location)
                        ?: Double.MAX_VALUE) < roadNetworkMaxDistance * roadNetworkMaxDistance
                }
                .map {
                    async {
                        val pathFinder = PatheticMapper.newPathfinder(
                            PathingRuleSet.createAsyncRuleSet()
                                .withMaxLength(roadNetworkMaxDistance.toInt())
                                .withLoadingChunks(true)
                                .withAllowingDiagonal(true)
                                .withAllowingFailFast(true)
                        )
                        val start = if (asEnd) it else node
                        val end = if (asEnd) node else it
                        val result = pathFinder.findPath(
                            start.location.toPathPosition(),
                            end.location.toPathPosition(),
                            NodeAvoidPathfindingStrategy(
                                nodeAvoidance = nodes - start - end,
                                permanentLock = true,
                                strategy = NodeAvoidPathfindingStrategy(
                                    nodeAvoidance = negativeNodes.filter {
                                        val distance = start.location.distanceSqrt(it.location) ?: 0.0
                                        distance > it.radius * it.radius && distance < roadNetworkMaxDistance * roadNetworkMaxDistance
                                    },
                                    permanentLock = false,
                                    strategy = WalkablePathfinderStrategy(),
                                ),
                            )
                        ).await()

                        if (result.hasFailed()) return@async null
                        RoadEdge(start.id, end.id, result.path.length().toDouble())
                    }
                }.awaitAll()
                .filterNotNull()
        }

    private fun constructNewNetwork(network: RoadNetwork, vararg pairs: Pair<RoadNode, List<RoadEdge>?>): RoadNetwork {
        val newNodes = network.nodes.toMutableList()
        val newEdges = network.edges.toMutableList()

        pairs.filter { it.second != null }.forEach { (node, edges) ->
            newNodes += node
            newEdges += edges.orEmpty()
        }

        return network.copy(
            nodes = newNodes,
            edges = newEdges
        )
    }

    private fun distanceWeight(startEnd: Double?, end: Location, current: Location): Double? {
        if (startEnd == null) return null
        val currentEnd = current.distanceSqrt(end) ?: return null
        if (currentEnd == 0.0) return 0.0
        return currentEnd / startEnd
    }
}

class VisitedNode(
    val edge: RoadEdge?, // From previous to this node
)

class InspectingNode(
    val node: RoadNodeId,
    val edge: RoadEdge?, // From previous to this node
    val weight: Double,
    private val distanceWeight: Double?,
) : Comparable<InspectingNode> {
    private val score: Double
        get() = weight + (distanceWeight ?: 0.0) * 50

    override fun compareTo(other: InspectingNode): Int = score.compareTo(other.score)
}