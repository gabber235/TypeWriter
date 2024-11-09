package com.typewritermc.basic.entries.static

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.ContentEditor
import com.typewritermc.engine.paper.entry.entries.RoadNetwork
import com.typewritermc.engine.paper.entry.entries.RoadNetworkEntry
import com.typewritermc.engine.paper.entry.entries.data
import com.typewritermc.engine.paper.entry.entries.hasData
import com.typewritermc.engine.paper.entry.roadnetwork.content.RoadNetworkContentMode

@Entry("base_road_network", "A definition of the words road network", Colors.YELLOW, "material-symbols:map")
/**
 * The `Simple Road Network` is a definition of a road network.
 * The road network is a system of interconnected nodes and edges that represent a network in the world.
 *
 * ## How could this be used?
 * To let npc's navigate the world.
 * And to let players navigate the world by showing them the way to go.
 */
class BaseRoadNetworkEntry(
    override val id: String = "",
    override val name: String = "",
    @ContentEditor(RoadNetworkContentMode::class)
    override val artifactId: String = "",
) : RoadNetworkEntry {
    override suspend fun loadRoadNetwork(gson: Gson): RoadNetwork {
        if (!hasData()) return RoadNetwork()
        return gson.fromJson(data(), object : TypeToken<RoadNetwork>() {}.type)
    }

    override suspend fun saveRoadNetwork(gson: Gson, network: RoadNetwork) {
        data(gson.toJson(network))
    }
}