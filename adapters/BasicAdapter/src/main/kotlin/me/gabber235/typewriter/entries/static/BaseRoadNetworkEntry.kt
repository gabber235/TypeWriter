package me.gabber235.typewriter.entries.static

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.ContentEditor
import me.gabber235.typewriter.entry.entries.RoadNetwork
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.entries.data
import me.gabber235.typewriter.entry.roadnetwork.content.RoadNetworkContentMode

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
    override val id: String,
    override val name: String,
    @ContentEditor(RoadNetworkContentMode::class)
    override val artifactId: String,
) : RoadNetworkEntry {
    override suspend fun loadRoadNetwork(gson: Gson): RoadNetwork {
        return gson.fromJson(data(), object : TypeToken<RoadNetwork>() {}.type)
    }

    override suspend fun saveRoadNetwork(gson: Gson, network: RoadNetwork) {
        data(gson.toJson(network))
    }
}