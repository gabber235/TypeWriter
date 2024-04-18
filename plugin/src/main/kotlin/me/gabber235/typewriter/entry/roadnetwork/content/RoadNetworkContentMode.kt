package me.gabber235.typewriter.entry.roadnetwork.content

import com.github.retrooper.packetevents.util.Vector3f
import kotlinx.coroutines.future.await
import lirand.api.extensions.inventory.meta
import me.gabber235.typewriter.content.ContentComponent
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.content.components.*
import me.gabber235.typewriter.content.entryId
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.roadnetwork.NodeAvoidPathfindingStrategy
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkEditorState
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.utils.*
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.koin.core.component.KoinComponent
import org.patheloper.api.pathing.configuration.PathingRuleSet
import org.patheloper.api.pathing.strategy.strategies.WalkablePathfinderStrategy
import org.patheloper.api.wrapper.PathPosition
import org.patheloper.mapping.PatheticMapper
import org.patheloper.mapping.bukkit.BukkitMapper
import java.util.*
import kotlin.math.pow

class RoadNetworkContentMode(context: ContentContext, player: Player) : ContentMode(context, player), KoinComponent {
    private lateinit var ref: Ref<RoadNetworkEntry>
    private lateinit var editorComponent: RoadNetworkEditorComponent

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
            if (highlighting) suffix += " <yellow>(highlighting)</yellow>"
            suffix += editorComponent.state.message

            title = "Editing Road Network$suffix"
            color = when {
                editorComponent.state == RoadNetworkEditorState.DIRTY -> BossBar.Color.RED
                editorComponent.state == RoadNetworkEditorState.CALCULATING -> BossBar.Color.PURPLE
                highlighting -> BossBar.Color.YELLOW
                else -> BossBar.Color.GREEN
            }
        }
        exit()
        +NetworkHighlightComponent(::toggleHighlight)
        +NetworkRecalculateAllEdgesComponent {
            editorComponent.recalculateEdges()
        }
        +NetworkAddNodeComponent(::addRoadNode, ::addNegativeNode)
        nodes({ network.nodes }, ::showingLocation) {
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

        nodes({ network.negativeNodes }, ::showingLocation) {
            item = ItemStack(Material.NETHERITE_BLOCK)
            glow = if (highlighting) NamedTextColor.BLACK else null
            scale = Vector3f(0.5f, 0.5f, 0.5f)
            onInteract {
                ContentModeTrigger(
                    context,
                    SelectedNegativeNodeContentMode(
                        context,
                        player,
                        ref,
                        it.id,
                        false
                    )
                ) triggerFor player
            }
        }
        +NegativeNodePulseComponent { network.negativeNodes }

        +NetworkEdgesComponent({ network.nodes }, { network.edges })
        return ok(Unit)
    }


    private fun toggleHighlight() {
        highlighting = !highlighting
    }

    private fun createNode(): RoadNode {
        val location = player.location.toCenterLocation().apply {
            if (block.type.isSolid) {
                // If you are standing on a slab or something, we want to place the node on top of it
                if (!up.block.type.isSolid) {
                    add(0.0, 1.0, 0.0)
                }
            }
            yaw = 0.0f
            pitch = 0.0f
        }
        var id: Int
        do {
            id = Random().nextInt(Int.MAX_VALUE)
        } while (network.nodes.any { it.id.id == id })
        return RoadNode(RoadNodeId(id), location, 1.0)
    }

    private fun addRoadNode() = DISPATCHERS_ASYNC.launch {
        val node = createNode()
        editorComponent.update { it.copy(nodes = it.nodes + node) }
        editorComponent.recalculateEdges()
        ContentModeTrigger(
            context,
            SelectedRoadNodeContentMode(context, player, ref, node.id, true)
        ) triggerFor player
    }

    private fun addNegativeNode() = DISPATCHERS_ASYNC.launch {
        val node = createNode()
        editorComponent.update { it.copy(negativeNodes = it.negativeNodes + node) }
        ContentModeTrigger(
            context,
            SelectedNegativeNodeContentMode(context, player, ref, node.id, true)
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

        editorComponent.update { roadNetwork ->
            roadNetwork.copy(edges = roadNetwork.edges.filter { it.start != node.id } + edges)
        }
    }

    override suspend fun tick() {
        super.tick()
        cycle++
    }

    override suspend fun dispose() {
        super.dispose()
    }

    private fun showingLocation(node: RoadNode): Location = node.location.clone().apply {
        yaw = (cycle % 360).toFloat()
    }
}

fun Location.toPathPosition(): PathPosition = BukkitMapper.toPathPosition(this)
fun PathPosition.toLocation(): Location = BukkitMapper.toLocation(this)

fun RoadNode.material(modifications: List<RoadModification>): Material {
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
    private val onAdd: () -> Unit = {},
    private val onAddNegative: () -> Unit = {},
) : ContentComponent, ItemsComponent {
    override fun items(player: Player): Map<Int, IntractableItem> {
        val addNodeItem = ItemStack(Material.DIAMOND).meta {
            name = "<green><b>Add Node"
            loreString = "<line> <gray>Click to add a new node to the road network"
        } onInteract {
            if (it.type.isClick) onAdd()
        }

        val addNegativeNodeItem = ItemStack(Material.NETHERITE_INGOT).meta {
            name = "<red><b>Add Negative Node"
            loreString = "<line> <gray>Click to add a new negative node to the road network"
        } onInteract {
            if (it.type.isClick) onAddNegative()
        }

        return mapOf(
            4 to addNodeItem,
            5 to addNegativeNodeItem
        )
    }

    override suspend fun initialize(player: Player) {}
    override suspend fun tick(player: Player) {}
    override suspend fun dispose(player: Player) {}
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
    override suspend fun initialize(player: Player) {
        cycle = 0
    }

    private fun refreshEdges(player: Player) {
        val nodes = fetchNodes().associateBy { it.id }
        showingEdges = fetchEdges()
            .filter {
                (nodes[it.start]?.location?.distanceSqrt(player.location)
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

        val progress = (cycle.toDouble() / EDGE_SHOW_DURATION).easeInOutQuad()
        showingEdges.forEach { edge ->
            val start = edge.startLocation
            val end = edge.endLocation
            for (i in 0..1) {
                val percentage = progress - i * 0.05
                val location = start.lerp(end, percentage)
                player.spawnParticle(Particle.REDSTONE, location, 1, DustOptions(edge.color, 1.0f))
            }
        }

        cycle++

        if (cycle > EDGE_SHOW_DURATION) {
            cycle = 0
        }
    }

    private fun Double.easeInOutQuad(): Double {
        return if (this < 0.5) 2 * this * this else -1 + (4 - 2 * this) * this
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

class NegativeNodePulseComponent(
    private val negativeNodes: () -> List<RoadNode>,
) : ContentComponent {
    private var cycle = 0
    private var showingNodes = emptyList<Pulse>()
    override suspend fun initialize(player: Player) {
    }

    companion object {
        private const val PULSE_DURATION = 30
    }

    override suspend fun tick(player: Player) {
        if (cycle == 0) {
            showingNodes = negativeNodes()
                .filter { (it.location.distanceSqrt(player.location) ?: Double.MAX_VALUE) < roadNetworkMaxDistance * roadNetworkMaxDistance }
                .map { Pulse(it.location, it.radius) }
        }

        val percentage = (cycle.toDouble() / PULSE_DURATION).easeOutBack()
        showingNodes.forEach { pulse ->
            val radius = percentage * (pulse.radius - 0.2)
            pulse.location.particleSphere(player, radius, Color.BLACK, phiDivisions = 8, thetaDivisions = 5)
        }

        cycle++
        if (cycle > PULSE_DURATION) {
            cycle = 0
        }
    }

    data class Pulse(val location: Location, val radius: Double)

    private fun Double.easeOutBack(): Double {
        val c1 = 1.70158
        val c3 = c1 + 1

        return 1 + c3 * (this - 1).pow(3.0) + c1 * (this - 1).pow(2.0)
    }

    override suspend fun dispose(player: Player) {
    }
}