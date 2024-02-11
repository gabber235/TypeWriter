package me.gabber235.typewriter.interaction

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon
import io.ktor.util.collections.*
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent.get
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PacketBlocker : PacketListenerAbstract() {
    private val blockers = ConcurrentHashMap<UUID, PlayerPacketBlocker>()

    fun initialize() {
        PacketEvents.getAPI().eventManager.registerListener(this)
    }

    override fun onPacketReceive(event: PacketReceiveEvent?) {
        if (event == null) return
        val player = event.player
        if (player !is Player) return
        val packetType = event.packetType
        val blocker = blockers[player.uniqueId] ?: return
        if (packetType in blocker) {
            event.isCancelled = true
        }
    }

    fun blockPacket(player: UUID, packetType: PacketTypeCommon) {
        blockers.compute(player) { _, blocker ->
            val newBlocker = blocker ?: PlayerPacketBlocker()
            newBlocker.block(packetType)
            newBlocker
        }
    }

    fun unblockPacket(player: UUID, packetType: PacketTypeCommon) {
        blockers.compute(player) { _, blocker ->
            val newBlocker = blocker ?: return@compute null
            if (newBlocker.unblock(packetType)) null else newBlocker
        }
    }

    fun shutdown() {
        PacketEvents.getAPI().eventManager.unregisterListener(this)
        blockers.clear()
    }
}

infix fun Player.blockPacket(packetType: PacketTypeCommon) {
    get<PacketBlocker>(PacketBlocker::class.java).blockPacket(uniqueId, packetType)
}

infix fun Player.unblockPacket(packetType: PacketTypeCommon) {
    get<PacketBlocker>(PacketBlocker::class.java).unblockPacket(uniqueId, packetType)
}


private data class PlayerPacketBlocker(
    val packets: ConcurrentMap<PacketTypeCommon, Int> = ConcurrentMap()
) {

    fun block(packetType: PacketTypeCommon) {
        packets.compute(packetType) { _, count -> (count ?: 0) + 1 }
    }

    fun unblock(packetType: PacketTypeCommon): Boolean {
        packets.compute(packetType) { _, count ->
            val newCount = (count ?: 0) - 1
            if (newCount <= 0) null else newCount
        }
        return packets.isEmpty()
    }

    operator fun contains(packetType: PacketTypeCommon) = packets.containsKey(packetType)
}