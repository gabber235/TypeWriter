package me.gabber235.typewriter.entry.entries

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.LocationSerializer
import me.gabber235.typewriter.utils.RuntimeTypeAdapterFactory
import org.bukkit.Location

val roadNetworkMaxDistance by snippet("road_network.distance.max", 30.0)

@Tags("road-network")
interface RoadNetworkEntry : ArtifactEntry {
    suspend fun loadRoadNetwork(gson: Gson): RoadNetwork
    suspend fun saveRoadNetwork(gson: Gson, network: RoadNetwork)
}

data class RoadNetwork(
    val nodes: List<RoadNode>,
    val edges: List<RoadEdge>,
    val modifications: List<RoadModification>,
)

@JvmInline
value class RoadNodeId(val id: Int) {
    override fun toString(): String = id.toString()
}

data class RoadNode(
    val id: RoadNodeId,
    val location: Location,
    val radius: Double,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoadNode

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

data class RoadEdge(
    val start: RoadNodeId,
    val end: RoadNodeId,
    val weight: Double,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoadEdge

        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }
}

sealed interface RoadModification {
    sealed interface EdgeModification : RoadModification {
        val start: RoadNodeId
        val end: RoadNodeId
    }

    data class EdgeAddition(
        override val start: RoadNodeId,
        override val end: RoadNodeId,
        val weight: Double
    ) :
        EdgeModification {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as EdgeAddition

            if (start != other.start) return false
            if (end != other.end) return false

            return true
        }

        override fun hashCode(): Int {
            var result = start.hashCode()
            result = 31 * result + end.hashCode()
            return result
        }
    }

    data class EdgeRemoval(override val start: RoadNodeId, override val end: RoadNodeId) :
        EdgeModification
}

fun Collection<RoadModification>.containsRemoval(start: RoadNodeId, end: RoadNodeId): Boolean =
    any { it is RoadModification.EdgeRemoval && it.start == start && it.end == end }

fun createRoadNetworkParser(): Gson = GsonBuilder()
    .registerTypeAdapter(Location::class.java, LocationSerializer())
    .registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(RoadModification::class.java)
            .registerSubtype(RoadModification.EdgeAddition::class.java)
            .registerSubtype(RoadModification.EdgeRemoval::class.java)
    )
    .create()
