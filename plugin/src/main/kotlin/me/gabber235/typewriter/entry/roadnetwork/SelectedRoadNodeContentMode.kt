package me.gabber235.typewriter.entry.roadnetwork

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
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import me.gabber235.typewriter.utils.loopingDistance
import me.gabber235.typewriter.utils.loreString
import me.gabber235.typewriter.utils.name
import me.gabber235.typewriter.utils.playSound
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.format.NamedTextColor
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
import org.koin.core.component.inject
import org.patheloper.api.pathing.configuration.PathingRuleSet
import org.patheloper.api.pathing.result.Path
import org.patheloper.mapping.PatheticMapper
import java.util.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.math.cos
import kotlin.math.sin

class SelectedRoadNodeContentMode(
    context: ContentContext,
    player: Player,
    private val ref: Ref<RoadNetworkEntry>,
    private var network: RoadNetwork,
    private var selectedNode: RoadNode,
    private val initiallyScrolling: Boolean,
) : ContentMode(context, player), KoinComponent {
    private val roadNetworkManager by inject<RoadNetworkManager>()
    private val modifications = network.modifications.toMutableList()

    // If the player has been in range of the node
    private var removedComponent = false
    private var cycle = 0
    override fun setup() {
        val networkSavingComponent = +NetworkSavingComponent(::ref) {
            roadNetwork()
        }
        val pathsComponent = +SelectedNodePathsComponent(::selectedNode, ::roadNetwork)
        bossBar {
            var suffix = ""
            if (networkSavingComponent.isDirty) suffix += " <gray><i>(unsaved changes)</i></gray>"
            else if (networkSavingComponent.isSaving) suffix = " <red><i>(saving)</i></red>"
            if (!pathsComponent.isPathsLoaded) suffix += " <gray><i>(calculating edges)</i></gray>"

            title = "Editing <gray>${selectedNode.id}</gray> node$suffix"
            color = when {
                networkSavingComponent.isDirty -> BossBar.Color.RED
                !pathsComponent.isPathsLoaded -> BossBar.Color.PURPLE
                else -> BossBar.Color.GREEN
            }
        }
        exit(doubleShiftExits = true)

        +NodeRadiusComponent(::selectedNode, initiallyScrolling) {
            selectedNode = selectedNode.copy(radius = (selectedNode.radius + it).coerceAtLeast(0.5))
            networkSavingComponent.isDirty = true
        }

        +RemoveNodeComponent {
            removedComponent = true
            networkSavingComponent.isDirty = true
            pop()
        }

        +ModificationComponent(context, ref, ::selectedNode, ::roadNetwork) { modification ->
            modifications.remove(modification)
            networkSavingComponent.isDirty = true
        }

        nodes({
            network.nodes.map { if (it == selectedNode) selectedNode else it }
        }, ::showingLocation) { node ->
            item = ItemStack(node.material(modifications))
            glow = if (node == selectedNode) NamedTextColor.GREEN else null
            scale = Vector3f(0.5f, 0.5f, 0.5f)
            onInteract(::pop)
        }
    }

    private fun roadNetwork() = if (removedComponent) {
        RoadNetwork(
            nodes = network.nodes - selectedNode,
            edges = network.edges.filter { it.start != selectedNode.id && it.end != selectedNode.id },
            modifications = modifications.filter {
                when (it) {
                    is RoadModification.EdgeModification -> it.start != selectedNode.id && it.end != selectedNode.id
                }
            }
        )
    } else {
        RoadNetwork(
            nodes = network.nodes - selectedNode + selectedNode,
            edges = network.edges,
            modifications = modifications
        )
    }

    private fun showingLocation(node: RoadNode): Location = node.location.clone().apply {
        yaw = (cycle % 360).toFloat()
    }

    private fun pop() {
        SystemTrigger.CONTENT_POP triggerFor player
    }

    override suspend fun initialize() {
        network = roadNetworkManager.getNetwork(ref)
        super.initialize()
    }

    override suspend fun tick() {
        cycle++

        super.tick()
    }
}

private class RemoveNodeComponent(
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
    private val nodeFetcher: () -> RoadNode,
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
        val node = nodeFetcher()
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
                    NodeAvoidPathfindingStrategy(nodeAvoidance = network.nodes - start - end)
                ).await()
                if (result.hasFailed()) return@mapNotNull null
                edge to result.path
            }
            .toMap()
    }


    private var tick = 0
    override suspend fun tick(player: Player) {

        if (paths == null) return
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

private class NodeRadiusComponent(
    private val nodeFetcher: () -> RoadNode,
    private val initiallyScrolling: Boolean,
    private val slot: Int = 2,
    private val editRadius: (Double) -> Unit,
) : ItemComponent, Listener {

    private var scrolling: UUID? = null

    override fun item(player: Player): Pair<Int, IntractableItem> {
        val item = if (scrolling != null) ItemStack(Material.CALIBRATED_SCULK_SENSOR).meta {
            name = "<yellow><b>Selecting Radius"
            loreString = "<line> <gray>Right click to set the radius of the node."
        } else ItemStack(Material.SCULK_SENSOR).meta {
            name = "<yellow><b>Change Radius"
            loreString = "<line> <gray>Current radius: <white>${nodeFetcher().radius}"
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
        val node = nodeFetcher()
        val radius = node.radius
        val location = node.location

        var phi = 0.0
        while (phi < Math.PI) {
            phi += Math.PI / 16
            var theta = 0.0
            while (theta < 2 * Math.PI) {
                theta += Math.PI / 8
                val x = radius * sin(phi) * cos(theta)
                val y = radius * cos(phi)
                val z = radius * sin(phi) * sin(theta)
                player.spawnParticle(
                    Particle.REDSTONE,
                    location.x + x,
                    location.y + y,
                    location.z + z,
                    1,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    DustOptions(Color.RED, radius.toFloat() / 2)
                )
            }
        }
    }

    override suspend fun dispose(player: Player) {
        super.dispose(player)
        unregister()
    }
}

private class ModificationComponent(
    private val context: ContentContext,
    private val ref: Ref<RoadNetworkEntry>,
    private val nodeFetcher: () -> RoadNode,
    private val networkFetcher: () -> RoadNetwork,
    private val removeModification: (RoadModification) -> Unit,
) : ContentComponent, ItemsComponent {
    override fun items(player: Player): Map<Int, IntractableItem> {
        val map = mutableMapOf<Int, IntractableItem>()
        val node = nodeFetcher()
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
                loreString = "<line> <gray>Click to add a fast travel connection to another node."
            } onInteract {
                ContentModeTrigger(
                    context,
                    EdgeModificationContentMode(
                        context,
                        player,
                        ref,
                        networkFetcher(),
                        "Select a node to create a <green>fast travel connection</green> to",
                        network.nodes
                            .asSequence()
                            .filter { target -> target.id != node.id }
                            .filter { target -> network.edges.none { it.start == node.id && it.end == target.id } }
                            .filter { target ->
                                network.modifications.none {
                                    it is RoadModification.EdgeModification && it.start == node.id && it.end == target.id
                                }
                            }
                            .toList()
                    ) { target -> RoadModification.EdgeAddition(node.id, target.id, 0.0) }
                ) triggerFor player
            }
        }

        val hasEdges = network.edges.any { it.start == node.id }
        if (hasEdges) {
            map[6] = ItemStack(Material.REDSTONE).meta {
                name = "<red><b>Remove Edge"
                loreString = "<line> <gray>Click to <red>remove an edge</red> from this node."
            } onInteract {
                val targetNodeIds = network.edges.filter { it.start == node.id }.map { it.end }
                ContentModeTrigger(
                    context,
                    EdgeModificationContentMode(
                        context,
                        player,
                        ref,
                        networkFetcher(),
                        "Select an edge to remove",
                        network.nodes.filter { target -> target.id in targetNodeIds }
                            .filter { target ->
                                network.modifications.none {
                                    it is RoadModification.EdgeModification && it.start == node.id && it.end == target.id
                                }
                            }
                    ) { target -> RoadModification.EdgeRemoval(node.id, target.id) }
                ) triggerFor player
            }
        }

        return map
    }
    private fun openMenu(player: Player) {
        plugin.chestMenu(6) {
            val node = nodeFetcher()
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