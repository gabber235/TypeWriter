package me.gabber235.typewriter.entry.roadnetwork.content

import com.github.retrooper.packetevents.util.Vector3f
import kotlinx.coroutines.future.await
import lirand.api.dsl.menu.builders.dynamic.chest.chestMenu
import lirand.api.dsl.menu.builders.dynamic.chest.pagination.pagination
import lirand.api.dsl.menu.builders.dynamic.chest.pagination.slot
import lirand.api.extensions.events.unregister
import lirand.api.extensions.inventory.meta
import lirand.api.extensions.inventory.set
import lirand.api.extensions.server.registerEvents
import me.gabber235.typewriter.content.ContentComponent
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.content.components.*
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.roadnetwork.NodeAvoidPathfindingStrategy
import me.gabber235.typewriter.entry.roadnetwork.RoadNetworkEditorState
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.*
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.ItemStack
import org.koin.core.component.KoinComponent
import org.patheloper.api.pathing.configuration.PathingRuleSet
import org.patheloper.api.pathing.result.Path
import org.patheloper.api.pathing.strategy.strategies.WalkablePathfinderStrategy
import org.patheloper.mapping.PatheticMapper
import java.util.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

class SelectedRoadNodeContentMode(
    context: ContentContext,
    player: Player,
    private val ref: Ref<RoadNetworkEntry>,
    private val selectedNodeId: RoadNodeId,
    private val initiallyScrolling: Boolean,
) : ContentMode(context, player), KoinComponent {
    private lateinit var editorComponent: RoadNetworkEditorComponent

    private val network get() = editorComponent.network
    private val selectedNode get() = network.nodes.find { it.id == selectedNodeId }

    private var cycle = 0

    override suspend fun setup(): Result<Unit> {
        editorComponent = +RoadNetworkEditorComponent(ref)

        val pathsComponent = +SelectedNodePathsComponent(::selectedNode, ::network)
        bossBar {
            var suffix = editorComponent.state.message
            if (!pathsComponent.isPathsLoaded) suffix += " <gray><i>(calculating edges)</i></gray>"

            title = "Editing <gray>${selectedNode?.id}</gray> node$suffix"
            color = when {
                editorComponent.state == RoadNetworkEditorState.DIRTY -> BossBar.Color.RED
                !pathsComponent.isPathsLoaded -> BossBar.Color.PURPLE
                else -> BossBar.Color.GREEN
            }
        }
        exit(doubleShiftExits = true)

        +NodeRadiusComponent(::selectedNode, initiallyScrolling) {
            editorComponent.updateAsync { roadNetwork ->
                roadNetwork.copy(nodes = roadNetwork.nodes.map { node ->
                    if (node.id == selectedNodeId) node.copy(
                        radius = (node.radius + it).coerceAtLeast(
                            0.5
                        )
                    ) else node
                })
            }
        }

        +RemoveNodeComponent {
            editorComponent.updateAsync { roadNetwork ->
                roadNetwork.copy(
                    nodes = roadNetwork.nodes.filter { it.id != selectedNodeId },
                    edges = roadNetwork.edges.filter { it.start != selectedNodeId && it.end != selectedNodeId },
                    modifications = roadNetwork.modifications.filter {
                        if (it !is RoadModification.EdgeModification) return@filter true
                        it.start != selectedNodeId && it.end != selectedNodeId
                    }
                )
            }
            SystemTrigger.CONTENT_POP triggerFor player
        }

        +ModificationComponent(::selectedNode, ::network) { modification ->
            editorComponent.updateAsync { roadNetwork ->
                roadNetwork.copy(
                    modifications = roadNetwork.modifications - modification
                )
            }
        }

        nodes({ network.nodes }, ::showingLocation) { node ->
            item = ItemStack(node.material(network.modifications))
            glow = when {
                node == selectedNode -> NamedTextColor.WHITE
                network.edges.any { it.start == selectedNodeId && it.end == node.id } -> NamedTextColor.BLUE
                network.modifications.containsRemoval(
                    selectedNodeId,
                    node.id
                ) && network.modifications.containsRemoval(node.id, selectedNodeId) -> NamedTextColor.RED

                network.modifications.containsRemoval(selectedNodeId, node.id) -> NamedTextColor.GOLD
                network.modifications.containsAddition(
                    selectedNodeId,
                    node.id
                ) && network.modifications.containsAddition(node.id, selectedNodeId) -> NamedTextColor.GREEN

                network.modifications.containsAddition(selectedNodeId, node.id) -> TextColor.color(0x4fec97)
                else -> null
            }
            scale = Vector3f(0.5f, 0.5f, 0.5f)
            onInteract { interactWithNode(node) }
        }

        nodes({ network.negativeNodes }, ::showingLocation) {
            item = ItemStack(Material.NETHERITE_BLOCK)
            glow = NamedTextColor.BLACK
            scale = Vector3f(0.5f, 0.5f, 0.5f)
            onInteract {
                ContentModeSwapTrigger(
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

        return ok(Unit)
    }

    private fun showingLocation(node: RoadNode): Location = node.location.clone().apply {
        yaw = (cycle % 360).toFloat()
    }

    private fun interactWithNode(node: RoadNode) {
        if (node == selectedNode) {
            SystemTrigger.CONTENT_POP triggerFor player
            return
        }

        if (player.inventory.heldItemSlot == 5) {
            edgeAddition(node)
            return
        }

        if (player.inventory.heldItemSlot == 6) {
            edgeRemoval(node)
            return
        }

        if (player.inventory.itemInMainHand.isEmpty) {
            ContentModeSwapTrigger(
                context,
                SelectedRoadNodeContentMode(context, player, ref, node.id, false),
            ) triggerFor player
            return
        }
    }

    /**
     * Toggle the edge between modified and unmodified bidirectional
     * When the player is shifting, then we want to do it only directionally
     */
    private inline fun <reified M : RoadModification.EdgeModification> edgeModification(
        node: RoadNode,
        create: (RoadNodeId, RoadNodeId) -> M,
        crossinline modifyNetwork: (RoadNode, RoadNode, RoadNetwork) -> RoadNetwork,
    ) {
        if (node == selectedNode) return
        // If it contains the other modification, we don't want to do anything
        val containsOtherModification =
            network.modifications.any {
                it is RoadModification.EdgeModification && it !is M
                        && it.start == selectedNodeId && it.end == node.id
            }
        if (containsOtherModification) return

        player.playSound("ui.button.click")

        val containsModification =
            network.modifications.any { it is M && it.start == selectedNodeId && it.end == node.id }

        val modification = create(selectedNodeId, node.id)
        val reverseModification = create(node.id, selectedNodeId)

        if (containsModification) {
            editorComponent.updateAsync { roadNetwork ->
                roadNetwork.copy(
                    modifications = roadNetwork.modifications.filter {
                        it != modification && if (player.isSneaking) it != reverseModification else true
                    }
                )
            }
        } else {
            val selectedNode = selectedNode ?: return
            editorComponent.updateAsync { roadNetwork ->
                val modifications = if (player.isSneaking) {
                    roadNetwork.modifications + modification
                } else {
                    roadNetwork.modifications + modification + reverseModification
                }

                val n1 = roadNetwork.copy(
                    modifications = modifications
                )
                if (player.isSneaking) {
                    modifyNetwork(selectedNode, node, n1)
                } else {
                    modifyNetwork(selectedNode, node, modifyNetwork(node, selectedNode, n1))
                }
            }
        }
    }

    private fun edgeAddition(node: RoadNode) {
        edgeModification(
            node,
            { start, end -> RoadModification.EdgeAddition(start, end, 0.0) }) { start, end, network ->
            network.copy(
                edges = network.edges + RoadEdge(start.id, end.id, 0.0)
            )
        }
    }

    private fun edgeRemoval(node: RoadNode) {
        edgeModification(node, { start, end -> RoadModification.EdgeRemoval(start, end) }) { start, end, network ->
            network.copy(
                edges = network.edges.filter { it.start != start.id || it.end != end.id }
            )
        }
    }

    override suspend fun tick() {
        cycle++

        super.tick()
    }
}

class RemoveNodeComponent(
    private val slot: Int = 0,
    private val onRemove: () -> Unit,
) : ItemComponent {
    override fun item(player: Player): Pair<Int, IntractableItem> {
        return slot to (ItemStack(Material.REDSTONE_BLOCK).meta {
            name = "<red><b>Remove Node"
            loreString = "<line> <gray>Careful! This action is irreversible."
        } onInteract {
            onRemove()
        })
    }
}

private class SelectedNodePathsComponent(
    private val nodeFetcher: () -> RoadNode?,
    private val networkFetcher: () -> RoadNetwork,
) : ContentComponent {
    private var paths: Map<RoadEdge, Path>? = null
    val isPathsLoaded: Boolean
        get() = paths != null

    override suspend fun initialize(player: Player) {
        DISPATCHERS_ASYNC.launch {
            paths = loadEdgePaths()
        }
    }

    private suspend fun loadEdgePaths(): Map<RoadEdge, Path> {
        val node = nodeFetcher() ?: return emptyMap()
        val network = networkFetcher()
        val nodes = network.nodes.associateBy { it.id }
        return network.edges.filter { it.start == node.id }
            .mapNotNull { edge ->
                val start = nodes[edge.start] ?: return@mapNotNull null
                val end = nodes[edge.end] ?: return@mapNotNull null
                val pathFinder = PatheticMapper.newPathfinder(
                    PathingRuleSet.createAsyncRuleSet()
                        .withMaxLength(roadNetworkMaxDistance.toInt())
                        .withLoadingChunks(true)
                        .withAllowingDiagonal(true)
                        .withAllowingFailFast(true)
                )
                val result = pathFinder.findPath(
                    start.location.toPathPosition(),
                    end.location.toPathPosition(),
                    NodeAvoidPathfindingStrategy(
                        nodeAvoidance = network.nodes - start - end,
                        permanentLock = true,
                        strategy = NodeAvoidPathfindingStrategy(
                            nodeAvoidance = network.negativeNodes,
                            permanentLock = false,
                            strategy = WalkablePathfinderStrategy(),
                        )
                    )
                ).await()
                if (result.hasFailed()) return@mapNotNull null
                edge to result.path
            }
            .toMap()
    }

    private suspend fun refreshEdges() {
        val node = nodeFetcher() ?: return
        val network = networkFetcher()
        val edges = network.edges.filter { it.start == node.id }
        if (paths?.keys?.toSet() == edges.toSet()) return
        paths = loadEdgePaths()
    }

    private var tick = 0
    override suspend fun tick(player: Player) {
        if (paths == null) return
        if (tick++ % 20 == 0) {
            refreshEdges()
        }
        if (tick++ % 3 != 0) return

        paths?.forEach { (edge, path) ->
            path.forEach {
                player.spawnParticle(
                    Particle.REDSTONE,
                    it.x + 0.5,
                    it.y + 0.5,
                    it.z + 0.5,
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    DustOptions(NetworkEdgesComponent.colorFromHash(edge.end.hashCode()), 1.0f)
                )
            }
        }
    }

    override suspend fun dispose(player: Player) {}
}

class NodeRadiusComponent(
    private val nodeFetcher: () -> RoadNode?,
    private val initiallyScrolling: Boolean,
    private val slot: Int = 2,
    private val color: Color = Color.RED,
    private val editRadius: (Double) -> Unit,
) : ItemComponent, Listener {

    private var scrolling: UUID? = null

    override fun item(player: Player): Pair<Int, IntractableItem> {
        val item = if (scrolling != null) ItemStack(Material.CALIBRATED_SCULK_SENSOR).meta {
            name = "<yellow><b>Selecting Radius"
            loreString = "<line> <gray>Right click to set the radius of the node."
            unClickable()
        } else ItemStack(Material.SCULK_SENSOR).meta {
            name = "<yellow><b>Change Radius"
            loreString = "<line> <gray>Current radius: <white>${nodeFetcher()?.radius}"
            unClickable()
        }
        return slot to (item onInteract {
            scrolling = if (scrolling == player.uniqueId) {
                null
            } else {
                player.uniqueId
            }
            player.playSound("ui.button.click")
        })
    }

    override suspend fun initialize(player: Player) {
        super.initialize(player)
        if (initiallyScrolling) {
            // When we start out already selecting, we want to make sure the player is holding the correct item
            // So that they can stop changing the radius
            player.inventory.heldItemSlot = slot
            scrolling = player.uniqueId
        }
        plugin.registerEvents(this)
    }

    @EventHandler
    private fun onScroll(event: PlayerItemHeldEvent) {
        if (event.player.uniqueId != scrolling) return
        val delta = loopingDistance(event.previousSlot, event.newSlot, 8)
        editRadius(delta * 0.5)
        event.player.playSound("block.note_block.hat", pitch = 1f + (delta * 0.1f), volume = 0.5f)
        event.isCancelled = true
    }

    private var tick: Int = 0
    override suspend fun tick(player: Player) {
        super.tick(player)
        if (tick++ % 2 == 0) return
        val node = nodeFetcher() ?: return
        val radius = node.radius
        val location = node.location

        location.particleSphere(player, radius, color, phiDivisions = 16, thetaDivisions = 8)
    }

    override suspend fun dispose(player: Player) {
        super.dispose(player)
        unregister()
    }
}

private class ModificationComponent(
    private val nodeFetcher: () -> RoadNode?,
    private val networkFetcher: () -> RoadNetwork,
    private val removeModification: (RoadModification) -> Unit,
) : ContentComponent, ItemsComponent {
    override fun items(player: Player): Map<Int, IntractableItem> {
        val map = mutableMapOf<Int, IntractableItem>()
        val node = nodeFetcher() ?: return map
        val network = networkFetcher()

        val hasModification =
            network.modifications.any { it is RoadModification.EdgeModification && it.start == node.id }
        if (hasModification) {
            map[4] = ItemStack(Material.BOOK).meta {
                name = "<blue><b>Manage Modifications"
                loreString = "<line> <gray>Click to manage the modifications of this node."
            } onInteract {
                openMenu(player)
            }
        }

        val hasNonConnectedNode =
            network.nodes.any { target -> target.id != node.id && network.edges.none { it.start == node.id && it.end == target.id } }

        if (hasNonConnectedNode) {
            map[5] = ItemStack(Material.EMERALD).meta {
                name = "<green><b>Add Fast Travel Connection"
                loreString = """
                    |<line> <gray>Click on a unconnected node to <green>add a fast travel connection</green> to it.
                    |<line> <gray>Click on a modified node to <red>remove the connection</red>.
                    |
                    |<line> <gray>If you only want to connect one way, hold <red>Shift</red> while clicking.
                    |""".trimMargin()
                unClickable()
            } onInteract {}
        }

        val hasEdges = network.edges.any { it.start == node.id }
        if (hasEdges) {
            map[6] = ItemStack(Material.REDSTONE).meta {
                name = "<red><b>Remove Edge"
                loreString = """
                    |<line> <gray>Click on a connected node to <red>force remove the edge</red> between them.
                    |<line> <gray>Click on a modified node to allow the edge to be added again.
                    |
                    |<line> <gray>If you only want to remove one way, hold <red>Shift</red> while clicking.
                """.trimMargin()
                unClickable()
            } onInteract {
            }
        }

        return map
    }

    private fun openMenu(player: Player) {
        plugin.chestMenu(6) {
            val node = nodeFetcher() ?: return@chestMenu
            title = "Modifications"
            val modifications = networkFetcher().modifications
                .filterIsInstance<RoadModification.EdgeModification>()
                .filter { it.start == node.id }

            pagination({ modifications }) {
                slot {
                    onRender { modification, _ ->
                        inventory[slotIndex] = when (modification) {
                            is RoadModification.EdgeAddition -> ItemStack(Material.EMERALD).meta {
                                name = "<green><b>Fast Travel Connection"
                                loreString = """
                                    |
                                    |<line> <gray>Target: <white>${modification.end}
                                    |<line> <gray>Weight: <white>${modification.weight}
                                    |
                                    |<line> <green><b>Left Click:</b> <white>Teleport to the target node.
                                    |<line> <red><b>Shift Right Click:</b> <white>Remove this modification.
                                """.trimMargin()
                            }

                            is RoadModification.EdgeRemoval -> ItemStack(Material.REDSTONE).meta {
                                name = "<red><b>Remove Edge"
                                loreString = """
                                    |
                                    |<line> <gray>Target: <white>${modification.end}
                                    |
                                    |<line> <green><b>Left Click:</b> <white>Teleport to the target node.
                                    |<line> <red><b>Shift Right Click:</b> <white>Remove this modification.
                                """.trimMargin()
                            }

                            null -> ItemStack(Material.AIR)
                        }
                    }
                    onInteract { modification, _ ->
                        if (modification == null) return@onInteract
                        when (click) {
                            ClickType.LEFT -> {
                                val target = networkFetcher().nodes.find { it.id == modification.end }
                                if (target != null) {
                                    player.teleport(target.location)
                                }
                            }

                            ClickType.SHIFT_RIGHT -> {
                                removeModification(modification)
                            }

                            else -> {}
                        }
                        close()
                    }
                }
            }
        }.open(player)
    }

    override suspend fun initialize(player: Player) {}

    override suspend fun tick(player: Player) {}

    override suspend fun dispose(player: Player) {}
}