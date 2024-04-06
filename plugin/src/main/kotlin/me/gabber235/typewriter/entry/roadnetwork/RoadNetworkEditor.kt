package me.gabber235.typewriter.entry.roadnetwork

import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.RoadNetwork
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RoadNetworkEditor(
    private val ref: Ref<out RoadNetworkEntry>,
) : KoinComponent {
    private val networkManager: RoadNetworkManager by inject()

    var network: RoadNetwork = RoadNetwork(emptyList(), emptyList(), emptyList())
        private set

    private var lastChange: Long = Long.MAX_VALUE
    private var job: Job? = null
    private var mutex = Mutex()

    val state: RoadNetworkEditorState
        get() = when {
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

    fun refresh() {
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
        networkManager.saveRoadNetwork(ref, network)
        lastChange = Long.MAX_VALUE
    }
}

enum class RoadNetworkEditorState {
    IDLE,
    DIRTY,
    SAVING,
}