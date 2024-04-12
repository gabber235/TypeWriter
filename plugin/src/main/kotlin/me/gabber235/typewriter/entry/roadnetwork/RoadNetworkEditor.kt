package me.gabber235.typewriter.entry.roadnetwork

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.roadnetwork.content.toPathPosition
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.patheloper.api.pathing.configuration.PathingRuleSet
import org.patheloper.api.pathing.strategy.strategies.WalkablePathfinderStrategy
import org.patheloper.mapping.PatheticMapper

class RoadNetworkEditor(
    private val ref: Ref<out RoadNetworkEntry>,
) : KoinComponent {
    private val networkManager: RoadNetworkManager by inject()

    var network: RoadNetwork = RoadNetwork()
        private set

    private var lastChange: Long = -1
    private var job: Job? = null
    private var jobRecalculateEdges: Job? = null
    private var mutex = Mutex()

    val state: RoadNetworkEditorState
        get() = when {
            lastChange < 0 -> RoadNetworkEditorState.LOADING
            job != null -> RoadNetworkEditorState.SAVING
            lastChange != Long.MAX_VALUE -> RoadNetworkEditorState.DIRTY
            else -> RoadNetworkEditorState.IDLE
        }

    fun load() {
        DISPATCHERS_ASYNC.launch {
            mutex.withLock {
                network = networkManager.getNetwork(ref)
                lastChange = Long.MAX_VALUE
            }
        }
    }

    suspend fun update(block: suspend (RoadNetwork) -> RoadNetwork) {
        mutex.withLock {
            network = block(network)
            lastChange = System.currentTimeMillis()
        }
    }

    fun recalculateEdges() {
        jobRecalculateEdges?.cancel()

        jobRecalculateEdges = DISPATCHERS_ASYNC.launch {
            update {
                it.copy(edges = emptyList())
            }
            coroutineScope {
                network.nodes.map {
                    launch {
                        recalculateEdgesForNode(it)
                    }
                }
            }
            jobRecalculateEdges = null
        }
    }

    private suspend fun recalculateEdgesForNode(node: RoadNode) {
        val interestingNodes = network.nodes
            .filter { it != node && it.location.world == node.location.world && it.location.distanceSquared(node.location) < roadNetworkMaxDistance * roadNetworkMaxDistance }
        val generatedEdges =
            interestingNodes
                .filter { !network.modifications.containsRemoval(node.id, it.id) }
                .mapNotNull { target ->
                    val pathFinder = PatheticMapper.newPathfinder(
                        PathingRuleSet.createAsyncRuleSet()
                            .withMaxLength(roadNetworkMaxDistance.toInt())
                            .withLoadingChunks(true)
                            .withAllowingDiagonal(true)
                            .withAllowingFailFast(true)
                    )
                    val result = pathFinder.findPath(
                        node.location.toPathPosition(),
                        target.location.toPathPosition(),
                        NodeAvoidPathfindingStrategy(
                            nodeAvoidance = interestingNodes - node - target,
                            permanentLock = true,
                            strategy = NodeAvoidPathfindingStrategy(
                                nodeAvoidance = network.negativeNodes,
                                permanentLock = false,
                                strategy = WalkablePathfinderStrategy(),
                            )
                        )
                    ).await()
                    if (result.hasFailed()) return@mapNotNull null
                    val weight = result.path.length()
                    RoadEdge(node.id, target.id, weight.toDouble())
                }

        val manualEdges = network.modifications
            .asSequence()
            .filterIsInstance<RoadModification.EdgeAddition>()
            .filter { it.start == node.id }
            .map { RoadEdge(it.start, it.end, it.weight) }

        val edges = (generatedEdges + manualEdges).toList()

        update { roadNetwork ->
            roadNetwork.copy(edges = roadNetwork.edges.filter { it.start != node.id } + edges)
        }
    }

    fun refresh() {
        if (lastChange < 0) return
        if (lastChange + 3_000 < System.currentTimeMillis() && job == null) {
            job = DISPATCHERS_ASYNC.launch {
                val network = mutex.withLock {
                    lastChange = Long.MAX_VALUE
                    network = network.copy(
                        nodes = network.nodes.distinct(),
                        edges = network.edges.distinct(),
                        modifications = network.modifications.distinct(),
                    )
                    network
                }
                networkManager.saveRoadNetwork(ref, network)
                job = null
            }
        }
    }

    suspend fun dispose() {
        job?.cancel()
        job = null
        jobRecalculateEdges?.cancel()
        jobRecalculateEdges = null
        networkManager.saveRoadNetwork(ref, network)
        lastChange = Long.MAX_VALUE
    }
}

enum class RoadNetworkEditorState(val message: String) {
    LOADING(" <gray><i>(loading)</i></gray>"),
    IDLE(""),
    DIRTY(" <gray><i>(unsaved changes)</i></gray>"),
    SAVING(" <red><i>(saving)</i></red>"),
    CALCULATING(" <red><i>(calculating)</i></red>"),
}