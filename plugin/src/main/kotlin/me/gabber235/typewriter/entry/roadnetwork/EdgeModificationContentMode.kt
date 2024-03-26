package me.gabber235.typewriter.entry.roadnetwork

import com.github.retrooper.packetevents.util.Vector3f
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.content.components.bossBar
import me.gabber235.typewriter.content.components.exit
import me.gabber235.typewriter.content.components.nodes
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EdgeModificationContentMode(
    context: ContentContext,
    player: Player,
    private val ref: Ref<RoadNetworkEntry>,
    private val network: RoadNetwork,
    private val title: String,
    private val possibleNodes: List<RoadNode>,
    private val createModification: (RoadNode) -> RoadModification.EdgeModification,
) : ContentMode(context, player), KoinComponent {
    private val roadNetworkManager by inject<RoadNetworkManager>()
    private var cycle = 0
    override fun setup() {
        exit(doubleShiftExits = true)

        bossBar {
            title = this@EdgeModificationContentMode.title
            color = BossBar.Color.GREEN
        }

        nodes(::possibleNodes, ::showingLocation) { node ->
            item = ItemStack(node.material(network.modifications))
            glow = NamedTextColor.WHITE
            scale = Vector3f(0.5f, 0.5f, 0.5f)
            onInteract { onInteract(node) }
        }
    }

    private fun onInteract(node: RoadNode) = DISPATCHERS_ASYNC.launch {
        val modification = createModification(node)

        val newEdges = when (modification) {
            is RoadModification.EdgeAddition -> network.edges + RoadEdge(
                modification.start,
                modification.end,
                modification.weight
            )

            is RoadModification.EdgeRemoval -> network.edges - RoadEdge(modification.start, modification.end, 0.0)
        }

        val newNetwork = network.copy(
            edges = newEdges,
            modifications = network.modifications + modification
        )
        roadNetworkManager.saveRoadNetwork(ref, newNetwork)
        SystemTrigger.CONTENT_POP triggerFor player
    }

    private fun showingLocation(node: RoadNode): Location = node.location.clone().apply {
        yaw = (cycle % 360).toFloat()
    }

    override suspend fun tick() {
        cycle++
        super.tick()
    }
}