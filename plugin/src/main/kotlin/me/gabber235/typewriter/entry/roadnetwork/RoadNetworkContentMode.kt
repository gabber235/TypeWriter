package me.gabber235.typewriter.entry.roadnetwork

import com.github.retrooper.packetevents.util.Vector3f
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.await
import lirand.api.extensions.inventory.meta
import me.gabber235.typewriter.content.ContentComponent
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.content.components.*
import me.gabber235.typewriter.content.entryId
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.utils.*
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.koin.core.component.KoinComponent
import org.patheloper.api.pathing.configuration.PathingRuleSet
import org.patheloper.api.wrapper.PathPosition
import org.patheloper.mapping.PatheticMapper
import org.patheloper.mapping.bukkit.BukkitMapper
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class RoadNetworkContentMode(context: ContentContext, player: Player) : ContentMode(context, player), KoinComponent {
    private lateinit var ref: Ref<RoadNetworkEntry>
    private lateinit var editorComponent: RoadNetworkEditorComponent

    private val jobs = ConcurrentLinkedQueue<Job>()

    private var cycle = 0L

    // If all nodes need to be highlighted
    private var highlighting = false

    private val network get() = editorComponent.network

    override suspend fun setup(): Result<Unit> {
        val entryId = context.entryId ?: return failure("No entry id found for RoadNetworkContentMode")

        ref = Ref(entryId, RoadNetworkEntry::class)
        ref.get() ?: return failure("No entry '$entryId' found for RoadNetworkContentMode")

        editorComponent = +RoadNetworkEditorComponent(ref)

        bossBar {
            var suffix = ""
            if (highlighting) suffix = " <yellow>(highlighting)</yellow>"
            if (editorComponent.state == RoadNetworkEditorState.DIRTY) suffix = " <gray><i>(unsaved changes)</i></gray>"
            else if (editorComponent.state == RoadNetworkEditorState.SAVING) suffix = " <red><i>(saving)</i></red>"
            if (jobs.isNotEmpty()) suffix = " <red><i>(calculating)</i></red>"
            title = "Editing Road Network$suffix"
            color = when {
                editorComponent.state == RoadNetworkEditorState.DIRTY -> BossBar.Color.RED
                jobs.isNotEmpty() -> BossBar.Color.PURPLE
                highlighting -> BossBar.Color.YELLOW
                else -> BossBar.Color.GREEN
            }
        }
        exit()
        +NetworkHighlightComponent(::toggleHighlight)
        +NetworkRecalculateAllEdgesComponent {
            jobs.add(DISPATCHERS_ASYNC.launch {
                editorComponent.update {
                    it.copy(edges = emptyList())
                }
                jobs.addAll(network.nodes.map {
                    DISPATCHERS_ASYNC.launch { calculateEdgesFor(it) }
                })
            })
        }
        +NetworkAddNodeComponent(::addNode)
        nodes({network.nodes}, ::showingLocation) {
            item = ItemStack(it.material(network.modifications))
            glow = if (highlighting) NamedTextColor.WHITE else null
            scale = Vector3f(0.5f, 0.5f, 0.5f)
            onInteract {
                ContentModeTrigger(
                    context,
                    SelectedRoadNodeContentMode(
                        context,
                        player,
                        ref,
                        it.id,
                        false
                    )
                ) triggerFor player
            }
        }
        +NetworkEdgesComponent({network.nodes}, {network.edges})
        return ok(Unit)
    }


    private fun toggleHighlight() {
        highlighting = !highlighting
    }

    private fun addNode() = DISPATCHERS_ASYNC.launch {
        val location = player.location.toCenterLocation().apply {
            yaw = 0.0f
            pitch = 0.0f
        }
        var id: Int
        do {
            id = Random().nextInt(Int.MAX_VALUE)
        } while (network.nodes.any { it.id.id == id })
        val node = RoadNode(RoadNodeId(id), location, 1.0)
        editorComponent.update { it.copy(nodes = it.nodes + node) }
        ContentModeTrigger(
            context,
            SelectedRoadNodeContentMode(context, player, ref, node.id, true)
        ) triggerFor player
    }

    private suspend fun calculateEdgesFor(node: RoadNode) {
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
                        NodeAvoidPathfindingStrategy(nodeAvoidance = interestingNodes - node - target)
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

        editorComponent.update { roadNetwork ->
            roadNetwork.copy(edges = roadNetwork.edges.filter { it.start != node.id } + edges)
        }
    }

    override suspend fun tick() {
        super.tick()
        cycle++

        jobs.removeIf { it.isCompleted }
    }

    override suspend fun dispose() {
        super.dispose()
        jobs.forEach { it.cancel() }
    }

    private fun showingLocation(node: RoadNode): Location = node.location.clone().apply {
        yaw = (cycle % 360).toFloat()
    }
}

internal fun Location.toPathPosition(): PathPosition = BukkitMapper.toPathPosition(this)
internal fun PathPosition.toLocation(): Location = BukkitMapper.toLocation(this)

internal fun RoadNode.material(modifications: List<RoadModification>): Material {
    val hasAdded = modifications.any { it is RoadModification.EdgeAddition && it.start == id }
    val hasRemoved = modifications.any { it is RoadModification.EdgeRemoval && it.start == id }
    return when {
        hasAdded && hasRemoved -> Material.GOLD_BLOCK
        hasAdded -> Material.EMERALD_BLOCK
        hasRemoved -> Material.REDSTONE_BLOCK
        else -> Material.DIAMOND_BLOCK
    }
}

private class NetworkAddNodeComponent(
    private val onAdd: () -> Unit = {}
) : ItemComponent {
    override fun item(player: Player): Pair<Int, IntractableItem> {
        val item = ItemStack(Material.DIAMOND).meta {
            name = "<green><b>Add Node"
            loreString = "<line> <gray>Click to add a new node to the road network"
        } onInteract {
            if (it.type.isClick) onAdd()
        }

        return 4 to item
    }
}

private class NetworkHighlightComponent(
    private val onHighlight: () -> Unit = {}
) : ItemComponent {
    override fun item(player: Player): Pair<Int, IntractableItem> {
        val item = ItemStack(Material.GLOWSTONE_DUST).meta {
            name = "<yellow><b>Highlight Nodes"
            loreString = "<line> <gray>Click to highlight all nodes"
        } onInteract {
            if (!it.type.isClick) return@onInteract
            onHighlight()
            player.playSound("ui.button.click")
        }

        return 0 to item
    }
}

private class NetworkRecalculateAllEdgesComponent(
    private val onRecalculate: () -> Unit = {}
) : ItemComponent {
    override fun item(player: Player): Pair<Int, IntractableItem> {
        val item = ItemStack(Material.REDSTONE).meta {
            name = "<red><b>Recalculate Edges"
            loreString = "<line> <gray>Click to recalculate all edges, this might take a while."
        } onInteract {
            if (!it.type.isClick) return@onInteract
            onRecalculate()
            player.playSound("ui.button.click")
        }

        return 1 to item
    }
}

internal class NetworkEdgesComponent(
    private val fetchNodes: () -> List<RoadNode>,
    private val fetchEdges: () -> List<RoadEdge>,
) : ContentComponent {
    private var cycle = 0
    private var showingEdges = emptyList<ShowingEdge>()
    override suspend fun initialize(player: Player) {}

    private fun refreshEdges(player: Player) {
        val nodes = fetchNodes().associateBy { it.id }
        showingEdges = fetchEdges()
            .filter {
                (nodes[it.start]?.location?.distanceSquared(player.location)
                    ?: Double.MAX_VALUE) < (roadNetworkMaxDistance * roadNetworkMaxDistance)
            }
            .mapNotNull { edge ->
                val start = nodes[edge.start]?.location ?: return@mapNotNull null
                val end = nodes[edge.end]?.location ?: return@mapNotNull null
                ShowingEdge(start, end, colorFromHash(edge.start.hashCode()))
            }
    }

    override suspend fun tick(player: Player) {
        if (cycle == 0) {
            refreshEdges(player)
        }

        val progress = cycle.toDouble() / EDGE_SHOW_DURATION
        showingEdges.forEach { edge ->
            val start = edge.startLocation
            val end = edge.endLocation
            for (i in 0..1) {
                val percentage = progress - i * 0.05
                val location = start.lerp(end, percentage)
                player.spawnParticle(Particle.REDSTONE, location, 1, Particle.DustOptions(edge.color, 1.0f))
            }
        }

        cycle++

        if (cycle > EDGE_SHOW_DURATION) {
            cycle = 0
        }
    }

    override suspend fun dispose(player: Player) {}

    class ShowingEdge(
        val startLocation: Location,
        val endLocation: Location,
        val color: Color = Color.RED,
    )

    companion object {
        const val EDGE_SHOW_DURATION = 50
        fun colorFromHash(hash: Int): Color {
            val r = (hash shr 16 and 0xFF) / 255.0
            val g = (hash shr 8 and 0xFF) / 255.0
            val b = (hash and 0xFF) / 255.0
            return Color.fromRGB((r * 255).toInt(), (g * 255).toInt(), (b * 255).toInt())
        }
    }
}