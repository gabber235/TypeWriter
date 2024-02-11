package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.utils.EquipmentSlotStateProvider
import me.gabber235.typewriter.utils.PlayerState
import me.gabber235.typewriter.utils.ThreadType.SYNC
import me.gabber235.typewriter.utils.restore
import me.gabber235.typewriter.utils.state
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

@Entry("pumpkin_hat_cinematic", "Show a pumpkin hat during a cinematic", Colors.CYAN, "mingcute:hat-fill")
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

    private var playerState: PlayerState? = null

    override suspend fun startSegment(segment: PumpkinHatSegment) {
        super.startSegment(segment)

        playerState = player.state(EquipmentSlotStateProvider(EquipmentSlot.HEAD))

        player.equipment.helmet = ItemStack(Material.CARVED_PUMPKIN)
    }

    override suspend fun stopSegment(segment: PumpkinHatSegment) {
        super.stopSegment(segment)

        SYNC.switchContext {
            player.restore(playerState)
            playerState = null
        }
    }

    override suspend fun teardown() {
        super.teardown()
        SYNC.switchContext {
            player.restore(playerState)
        }
    }
}