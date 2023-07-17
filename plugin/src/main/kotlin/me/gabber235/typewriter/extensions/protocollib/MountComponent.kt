package me.gabber235.typewriter.extensions.protocollib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer

class MountComponent(private val entityId: Int) {
	var riders: Set<Int> = emptySet()
		private set

	fun mount(riderId: Int): PacketContainer {
		riders = riders + riderId
		return createRidersPacket()
	}

	fun unmount(riderId: Int): PacketContainer {
		riders = riders - riderId
		return createRidersPacket()
	}

	private fun createRidersPacket(): PacketContainer {
		return PacketContainer(PacketType.Play.Server.MOUNT).apply {
			integers.write(0, entityId)
			integerArrays.write(0, riders.toIntArray())
		}
	}
}