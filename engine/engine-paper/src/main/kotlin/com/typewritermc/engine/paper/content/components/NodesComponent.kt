package com.typewritermc.engine.paper.content.components

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.player.InteractionHand
import com.github.retrooper.packetevents.util.Vector3f
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import lirand.api.extensions.events.unregister
import lirand.api.extensions.server.registerEvents
import com.typewritermc.engine.paper.content.ComponentContainer
import com.typewritermc.engine.paper.content.ContentComponent
import com.typewritermc.engine.paper.events.AsyncFakeEntityInteract
import com.typewritermc.engine.paper.extensions.packetevents.meta
import com.typewritermc.engine.paper.extensions.packetevents.toPacketItem
import com.typewritermc.engine.paper.extensions.packetevents.toPacketLocation
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.distanceSqrt
import me.tofaa.entitylib.meta.display.ItemDisplayMeta
import me.tofaa.entitylib.meta.other.InteractionMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import kotlin.math.max

const val NODE_SHOW_DISTANCE_SQUARED = 50 * 50

fun <N> ComponentContainer.nodes(
    nodeFetcher: () -> Collection<N>,
    nodeLocation: (N) -> Location,
    builder: NodeDisplayBuilder.(N) -> Unit
) = +NodesComponent(nodeFetcher, nodeLocation, builder)

class NodesComponent<N>(
    private val nodeFetcher: () -> Collection<N>,
    private val nodeLocation: (N) -> Location,
    private val builder: NodeDisplayBuilder.(N) -> Unit
) : ContentComponent, Listener {
    private val nodes = mutableMapOf<N, NodeDisplay>()
    private var lastRefresh = 0

    private fun refreshNodes(player: Player) {
        val newNodes = nodeFetcher()
            .filter {
                (nodeLocation(it).distanceSqrt(player.location) ?: Double.MAX_VALUE) < NODE_SHOW_DISTANCE_SQUARED
            }
            .toSet()

        val toRemove = nodes.keys - newNodes
        val toRefresh = nodes.keys.intersect(newNodes)
        val toAdd = newNodes - nodes.keys
        toRemove.forEach { nodes.remove(it)?.dispose() }
        toAdd.forEach { n ->
            nodes[n] = NodeDisplayBuilder()
                .apply { builder(n) }
                .run {
                    val display = NodeDisplay()
                    display.apply(this, nodeLocation(n))
                    display
                }
                .also { it.show(player, nodeLocation(n)) }
        }
        toRefresh.forEach { n -> nodes[n]?.apply(NodeDisplayBuilder().apply { builder(n) }, nodeLocation(n)) }
        lastRefresh = 0
    }

    override suspend fun initialize(player: Player) {
        plugin.registerEvents(this)
        refreshNodes(player)
    }

    override suspend fun tick(player: Player) {
        if (lastRefresh++ > 20) {
            refreshNodes(player)
        }
    }

    @EventHandler
    private fun onFakeEntityInteract(event: AsyncFakeEntityInteract) {
        if (event.hand != InteractionHand.MAIN_HAND || event.action == WrapperPlayClientInteractEntity.InteractAction.INTERACT_AT) return
        val entityId = event.entityId
        nodes.values.firstOrNull { it.entityId == entityId }?.interact()
    }

    override suspend fun dispose(player: Player) {
        unregister()
        nodes.values.forEach { it.dispose() }
        nodes.clear()
    }
}

class NodeDisplayBuilder {
    var item: ItemStack = ItemStack(Material.STONE)
    var glow: TextColor? = null
    var interaction: () -> Unit = {}
    var scale: Vector3f = Vector3f(1.0f, 1.0f, 1.0f)

    fun onInteract(action: () -> Unit) {
        interaction = action
    }
}

private class NodeDisplay {
    private val itemDisplay =
        WrapperEntity(EntityTypes.ITEM_DISPLAY)
    private val interaction =
        WrapperEntity(EntityTypes.INTERACTION)
    private var onInteract: () -> Unit = {}
    val entityId: Int
        get() = interaction.entityId

    fun apply(builder: NodeDisplayBuilder, location: Location) {
        itemDisplay.meta<ItemDisplayMeta> {
            item = builder.item.toPacketItem()
            isGlowing = builder.glow != null
            glowColorOverride = builder.glow?.value() ?: -1
            scale = builder.scale
            positionRotationInterpolationDuration = 30
        }
        interaction.meta<InteractionMeta> {
            width = max(builder.scale.x, builder.scale.z)
            height = builder.scale.y
        }
        onInteract = builder.interaction
        if (itemDisplay.isSpawned) {
            itemDisplay.teleport(location.toPacketLocation())
        }
        if (interaction.isSpawned &&
            (interaction.location.x != location.x
                    || interaction.location.y != (location.y - builder.scale.y / 2)
                    || interaction.location.z != location.z)
        ) {
            interaction.teleport(location
                .clone()
                .apply { y -= builder.scale.y / 2 }
                .toPacketLocation())
        }
    }

    fun show(player: Player, location: Location) {
        itemDisplay.addViewer(player.uniqueId)
        itemDisplay.spawn(location.toPacketLocation())
        interaction.addViewer(player.uniqueId)
        interaction.spawn(location.toPacketLocation())
    }

    fun interact() {
        onInteract()
    }

    fun dispose() {
        itemDisplay.despawn()
        itemDisplay.remove()
        interaction.despawn()
        interaction.remove()
    }
}