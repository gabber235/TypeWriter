package me.gabber235.typewriter.extensions.protocollib

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import lirand.api.extensions.server.server
import org.bukkit.entity.Player
import java.util.*

val protocolManager = ProtocolLibrary.getProtocolManager()

fun Player.sendPacket(packet: PacketContainer) {
    protocolManager.sendServerPacket(this, packet)
}

class ViewersComponent {
    private val viewers = mutableSetOf<UUID>()


    fun addViewer(uuid: UUID) {
        viewers.add(uuid)
    }

    fun removeViewer(uuid: UUID) {
        viewers.remove(uuid)
    }

    fun sendPacket(packet: PacketContainer) {
        viewers.mapNotNull {
            server.getPlayer(it)
        }.forEach {
            it.sendPacket(packet)
        }
    }

    fun sendPacket(uuid: UUID, packet: PacketContainer) {
        if (!viewers.contains(uuid)) return

        server.getPlayer(uuid)?.sendPacket(packet)
    }
}