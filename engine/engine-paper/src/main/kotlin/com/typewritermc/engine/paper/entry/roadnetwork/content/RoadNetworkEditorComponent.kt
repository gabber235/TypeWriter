package com.typewritermc.engine.paper.entry.roadnetwork.content

import com.typewritermc.engine.paper.content.ContentComponent
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entries.RoadNetwork
import com.typewritermc.engine.paper.entry.entries.RoadNetworkEntry
import com.typewritermc.engine.paper.entry.roadnetwork.RoadNetworkEditorState
import com.typewritermc.engine.paper.entry.roadnetwork.RoadNetworkManager
import com.typewritermc.engine.paper.utils.ThreadType
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