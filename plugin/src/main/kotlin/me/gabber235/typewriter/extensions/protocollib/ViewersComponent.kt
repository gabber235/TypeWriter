package me.gabber235.typewriter.extensions.protocollib

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import lirand.api.extensions.server.server
import java.util.*

class ViewersComponent {
	private val manager = ProtocolLibrary.getProtocolManager()
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
			manager.sendServerPacket(it, packet)
		}
	}

	fun sendPacket(uuid: UUID, packet: PacketContainer) {
		if (!viewers.contains(uuid)) return

		server.getPlayer(uuid)?.let {
			manager.sendServerPacket(it, packet)
		}
	}
}