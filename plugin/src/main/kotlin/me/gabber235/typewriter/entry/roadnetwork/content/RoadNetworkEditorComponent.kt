package me.gabber235.typewriter.entry.roadnetwork.content

import me.gabber235.typewriter.content.ContentComponent
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.RoadNetwork
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkEditorState
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkManager
import me.gabber235.typewriter.utils.ThreadType
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RoadNetworkEditorComponent(
    private val ref: Ref<out RoadNetworkEntry>,
) : ContentComponent, KoinComponent {
    private val networkManager: RoadNetworkManager by inject()
    val network: RoadNetwork
        get() = networkManager.getEditorNetwork(ref).network

    val state: RoadNetworkEditorState
        get() = networkManager.getEditorNetwork(ref).state

    suspend fun update(block: suspend (RoadNetwork) -> RoadNetwork) {
        networkManager.getEditorNetwork(ref).update(block)
    }

    fun updateAsync(block: suspend (RoadNetwork) -> RoadNetwork) {
        ThreadType.DISPATCHERS_ASYNC.launch {
            update(block)
        }
    }

    fun recalculateEdges() = networkManager.getEditorNetwork(ref).recalculateEdges()

    override suspend fun initialize(player: Player) {}

    override suspend fun tick(player: Player) {}

    override suspend fun dispose(player: Player) {}
}