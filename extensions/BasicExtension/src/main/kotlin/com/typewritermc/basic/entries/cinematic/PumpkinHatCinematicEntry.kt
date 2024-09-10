package com.typewritermc.basic.entries.cinematic

import com.github.retrooper.packetevents.protocol.player.Equipment
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.cinematic.SimpleCinematicAction
import com.typewritermc.engine.paper.entry.entries.CinematicAction
import com.typewritermc.engine.paper.entry.entries.PrimaryCinematicEntry
import com.typewritermc.engine.paper.entry.entries.Segment
import com.typewritermc.engine.paper.extensions.packetevents.sendPacketTo
import com.typewritermc.engine.paper.extensions.packetevents.toPacketItem
import com.typewritermc.engine.paper.utils.name
import com.typewritermc.engine.paper.utils.unClickable
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Entry("pumpkin_hat_cinematic", "Show a pumpkin hat during a cinematic", Colors.CYAN, "mingcute:hat-fill")
/**
 * The `Pumpkin Hat Cinematic` is a cinematic that shows a pumpkin hat on the player's head.
 *
 * ## How could this be used?
 * When you have a resource pack, you can re-texture the pumpkin overlay to make it look like cinematic black bars.
 */
class PumpkinHatCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(icon = "mingcute:hat-fill")
    val segments: List<PumpkinHatSegment> = emptyList(),
) : PrimaryCinematicEntry {
    override fun create(player: Player): CinematicAction {
        return PumpkinHatCinematicAction(
            player,
            this,
        )
    }
}

data class PumpkinHatSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
) : Segment

class PumpkinHatCinematicAction(
    private val player: Player,
    entry: PumpkinHatCinematicEntry,
) : SimpleCinematicAction<PumpkinHatSegment>() {
    override val segments: List<PumpkinHatSegment> = entry.segments

    override suspend fun startSegment(segment: PumpkinHatSegment) {
        super.startSegment(segment)

        WrapperPlayServerEntityEquipment(
            player.entityId,
            listOf(
                Equipment(
                    com.github.retrooper.packetevents.protocol.player.EquipmentSlot.HELMET,
                    ItemStack(Material.CARVED_PUMPKIN)
                        .apply {
                            editMeta { meta ->
                                meta.name = " "
                                meta.unClickable()
                            }
                        }
                        .toPacketItem()
                )
            )
        ) sendPacketTo player
    }

    override suspend fun stopSegment(segment: PumpkinHatSegment) {
        super.stopSegment(segment)
        WrapperPlayServerEntityEquipment(
            player.entityId,
            listOf(
                Equipment(
                    com.github.retrooper.packetevents.protocol.player.EquipmentSlot.HELMET,
                    player.inventory.helmet?.toPacketItem()
                        ?: com.github.retrooper.packetevents.protocol.item.ItemStack.EMPTY
                )
            )
        ) sendPacketTo player
    }
}