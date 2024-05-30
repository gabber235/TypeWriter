package me.gabber235.typewriter.entry.entries

import com.github.retrooper.packetevents.util.Vector3f
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.ContentEditor
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.content.components.bossBar
import me.gabber235.typewriter.content.components.exit
import me.gabber235.typewriter.content.components.nodes
import me.gabber235.typewriter.content.entryId
import me.gabber235.typewriter.content.fieldPath
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.roadnetwork.content.RoadNetworkEditorComponent
import me.gabber235.typewriter.entry.roadnetwork.content.material
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.koin.java.KoinJavaComponent
import java.util.Map.entry

val roadNetworkMaxDistance by snippet("road_network.distance.max", 30.0)

@Tags("road-network")
interface RoadNetworkEntry : ArtifactEntry {
    suspend fun loadRoadNetwork(gson: Gson): RoadNetwork
    suspend fun saveRoadNetwork(gson: Gson, network: RoadNetwork)
}

@Tags("road-network-node")
interface RoadNodeEntry : Entry {
    val roadNetwork: Ref<RoadNetworkEntry>

    @ContentEditor(SelectRoadNodeContentMode::class)
    val nodeId: RoadNodeId
}

@Tags("road-network-node-collection")
interface RoadNodeCollectionEntry : Entry {
    val roadNetwork: Ref<RoadNetworkEntry>

    @ContentEditor(SelectRoadNodeCollectionContentMode::class)
    val nodes: List<RoadNodeId>
}

data class RoadNetwork(
    val nodes: List<RoadNode> = emptyList(),
    val edges: List<RoadEdge> = emptyList(),
    val modifications: List<RoadModification> = emptyList(),
    val negativeNodes: List<RoadNode> = emptyList(),
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

fun Collection<RoadEdge>.containsEdge(start: RoadNodeId, end: RoadNodeId): Boolean =
    any { it.start == start && it.end == end }

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

fun Collection<RoadModification>.containsAddition(start: RoadNodeId, end: RoadNodeId): Boolean =
    any { it is RoadModification.EdgeAddition && it.start == start && it.end == end }

fun createRoadNetworkParser(): Gson = GsonBuilder()
    .registerTypeAdapter(Location::class.java, LocationSerializer())
    .registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(RoadModification::class.java)
            .registerSubtype(RoadModification.EdgeAddition::class.java)
            .registerSubtype(RoadModification.EdgeRemoval::class.java)
    )
    .create()

class SelectRoadNodeContentMode(context: ContentContext, player: Player) : ContentMode(context, player) {
    private lateinit var editorComponent: RoadNetworkEditorComponent
    private val network: RoadNetwork
        get() = editorComponent.network

    private var cycle = 0

    override suspend fun setup(): Result<Unit> {
        val fieldPath = context.fieldPath ?: return failure(Exception("No field path found"))
        val entryId = context.entryId ?: return failure(Exception("No entry id found"))

        val entry = Query.findById<RoadNodeEntry>(entryId)
            ?: return failure(Exception("No road node found with id $entryId"))

        val roadNetworkRef = entry.roadNetwork

        if (!roadNetworkRef.isSet) {
            return failure("No road network found with id ${entry.roadNetwork.id} associated with road node $entryId")
        }

        editorComponent = RoadNetworkEditorComponent(roadNetworkRef)

        exit(doubleShiftExits = true)
        bossBar {
            title = "Select Road Node"
            color = BossBar.Color.WHITE
        }

        nodes({ network.nodes }, ::showingLocation) { node ->
            item = ItemStack(node.material(network.modifications))
            glow = NamedTextColor.WHITE
            scale = Vector3f(0.5f, 0.5f, 0.5f)

            onInteract {
                val value = node.id.id
                entry.ref().fieldValue(fieldPath, value)
                SystemTrigger.CONTENT_POP triggerFor player
            }
        }

        return ok(Unit)
    }

    override suspend fun tick() {
        super.tick()
        cycle++
    }

    private fun showingLocation(node: RoadNode): Location = node.location.clone().apply {
        yaw = (cycle % 360).toFloat()
    }
}

class SelectRoadNodeCollectionContentMode(context: ContentContext, player: Player) : ContentMode(context, player) {
    private lateinit var editorComponent: RoadNetworkEditorComponent
    private val network: RoadNetwork
        get() = editorComponent.network

    private var cycle = 0

    private var nodes: List<RoadNodeId> = emptyList()

    override suspend fun setup(): Result<Unit> {
        val fieldPath = context.fieldPath ?: return failure(Exception("No field path found"))
        val entryId = context.entryId ?: return failure(Exception("No entry id found"))

        val entry = Query.findById<RoadNodeCollectionEntry>(entryId)
            ?: return failure(Exception("No road node collection found with id $entryId"))

        nodes = entry.nodes
        val ref = entry.ref()

        val roadNetworkRef = entry.roadNetwork

        if (!roadNetworkRef.isSet) {
            return failure("No road network found with id ${entry.roadNetwork.id} associated with road node collection $entryId")
        }


        editorComponent = RoadNetworkEditorComponent(roadNetworkRef)

        exit(doubleShiftExits = true)
        bossBar {
            title = "Select Road Nodes <gray>(${nodes.size})"
            color = BossBar.Color.WHITE
        }

        nodes({ network.nodes }, ::showingLocation) { node ->
            item = ItemStack(node.material(network.modifications))
            glow = when {
                nodes.any { it.id == node.id.id } -> NamedTextColor.BLUE
                else -> NamedTextColor.WHITE
            }
            scale = Vector3f(0.5f, 0.5f, 0.5f)

            onInteract {
                val value = node.id.id
                val newNodes = if (nodes.any { it.id == value }) {
                    nodes.filter { it.id != value }
                } else {
                    nodes + RoadNodeId(value)
                }
                ref.fieldValue(fieldPath, newNodes)
                nodes = newNodes
                player.playSound("ui.button.click")
            }
        }

        return ok(Unit)
    }

    override suspend fun tick() {
        super.tick()
        cycle++
    }

    private fun showingLocation(node: RoadNode): Location = node.location.clone().apply {
        yaw = (cycle % 360).toFloat()
    }
}