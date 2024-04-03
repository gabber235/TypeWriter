package me.gabber235.typewriter.entries.cinematic

import com.github.retrooper.packetevents.protocol.player.Equipment
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.extensions.packetevents.sendPacketTo
import me.gabber235.typewriter.extensions.packetevents.toPacketItem
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
    ) : CinematicEntry {
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
                    ItemStack(Material.CARVED_PUMPKIN).toPacketItem()
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
        )
    }
}