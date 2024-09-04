package com.typewritermc.basic.entries.cinematic

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.cinematic.SimpleCinematicAction
import com.typewritermc.engine.paper.entry.entries.CinematicAction
import com.typewritermc.engine.paper.entry.entries.CinematicEntry
import com.typewritermc.engine.paper.entry.entries.Segment
import com.typewritermc.engine.paper.extensions.packetevents.sendPacketTo
import com.typewritermc.engine.paper.utils.toBukkitLocation
import com.typewritermc.engine.paper.utils.toPacketVector3i
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import org.bukkit.Material
import org.bukkit.entity.Player

@Entry("set_fake_block_cinematic", "Set a fake block", Colors.CYAN, "mingcute:cube-3d-fill")
class SetFakeBlockCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(icon = "mingcute:cube-3d-fill")
    @Help("The segments that will be displayed in the cinematic")
    val segments: List<SetFakeBlockSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return SetFakeBlockCinematicAction(player, this)
    }
}

data class SetFakeBlockSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    val location: Position = Position.ORIGIN,
    val block: Material = Material.AIR,
) : Segment

class SetFakeBlockCinematicAction(
    private val player: Player,
    entry: SetFakeBlockCinematicEntry,
) : SimpleCinematicAction<SetFakeBlockSegment>() {
    override val segments: List<SetFakeBlockSegment> = entry.segments

    override suspend fun startSegment(segment: SetFakeBlockSegment) {
        super.startSegment(segment)

        val state = SpigotConversionUtil.fromBukkitBlockData(segment.block.createBlockData())
        val packet = WrapperPlayServerBlockChange(segment.location.toPacketVector3i(), state.globalId)
        packet.sendPacketTo(player)
    }

    override suspend fun stopSegment(segment: SetFakeBlockSegment) {
        super.stopSegment(segment)

        val bukkitLocation = segment.location.toBukkitLocation()
        player.sendBlockChange(bukkitLocation, bukkitLocation.block.blockData)
    }
}

