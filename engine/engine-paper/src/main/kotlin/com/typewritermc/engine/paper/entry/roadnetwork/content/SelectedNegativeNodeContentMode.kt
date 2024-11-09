package com.typewritermc.engine.paper.entry.roadnetwork.content

import com.github.retrooper.packetevents.util.Vector3f
import com.typewritermc.engine.paper.content.ContentContext
import com.typewritermc.engine.paper.content.ContentMode
import com.typewritermc.engine.paper.content.components.bossBar
import com.typewritermc.engine.paper.content.components.exit
import com.typewritermc.engine.paper.content.components.nodes
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.entry.forceTriggerFor
import com.typewritermc.engine.paper.entry.roadnetwork.RoadNetworkEditorState
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.core.utils.ok
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SelectedNegativeNodeContentMode(
    context: ContentContext,
    player: Player,
    private val ref: Ref<RoadNetworkEntry>,
    private val selectedNodeId: RoadNodeId,
    private val initiallyScrolling: Boolean,
) : ContentMode(context, player) {
    private lateinit var editorComponent: RoadNetworkEditorComponent

    private val network get() = editorComponent.network
    private val selectedNode get() = network.negativeNodes.find { it.id == selectedNodeId }

    private var cycle = 0

    override suspend fun setup(): Result<Unit> {
        editorComponent = +RoadNetworkEditorComponent(ref)

        bossBar {
            val suffix = editorComponent.state.message

            title = "Editing <gray>${selectedNode?.id}</gray> node$suffix"
            color = when {
                editorComponent.state == RoadNetworkEditorState.Dirty -> BossBar.Color.RED
                else -> BossBar.Color.GREEN
            }
        }
        exit(doubleShiftExits = true)

        +NodeRadiusComponent(::selectedNode, initiallyScrolling, slot = 4, color = Color.BLACK) {
            editorComponent.updateAsync { roadNetwork ->
                roadNetwork.copy(negativeNodes = roadNetwork.negativeNodes.map { node ->
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
                    negativeNodes = roadNetwork.negativeNodes.filter { it.id != selectedNodeId }
                )
            }
        }

        nodes({ network.negativeNodes }, ::showingLocation) {
            item = ItemStack(Material.NETHERITE_BLOCK)
            glow = if (it.id == selectedNodeId) NamedTextColor.BLACK else null
            scale = Vector3f(0.5f, 0.5f, 0.5f)
            onInteract {
                if (it.id == selectedNodeId) {
                    SystemTrigger.CONTENT_POP triggerFor player
                    return@onInteract
                }
                ContentModeSwapTrigger(
                    context,
                    SelectedNegativeNodeContentMode(context, player, ref, it.id, false),
                ) triggerFor player
            }
        }
        +NegativeNodePulseComponent { network.negativeNodes.filter { it.id != selectedNodeId } }

        return ok(Unit)
    }

    override suspend fun tick() {
        super.tick()
        if (selectedNode == null) {
            // If the node is no longer in the network, we want to pop the content
            SystemTrigger.CONTENT_POP forceTriggerFor  player
        }

        cycle++
    }

    private fun showingLocation(node: RoadNode): Location = node.location.clone().apply {
        yaw = (cycle % 360).toFloat()
    }
}