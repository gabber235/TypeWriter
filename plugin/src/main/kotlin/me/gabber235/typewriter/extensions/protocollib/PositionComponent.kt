package me.gabber235.typewriter.extensions.protocollib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.util.Vector

class PositionComponent(
	private val entityId: Int,
	private val entityType: EntityType,
	location: Location,
	velocity: Vector
) {
	var location: Location = location
		private set

	var velocity: Vector = velocity
		private set

	fun createShowPackets(): List<PacketContainer> {
		return listOf(
			createUpdateEntityPositionAndRotationPacket(location),
		)
	}

	fun move(location: Location): PacketContainer {
		// If the location is delta is less than 8, should update the entity's position and rotation. Otherwise, teleport.
		val packet = if (location.distanceSquared(this.location) < 8) {
			createUpdateEntityPositionAndRotationPacket(location)
		} else {
			createTeleportEntityPacket(location)
		}

		this.location = location
		return packet
	}

	fun rotate(yaw: Float, pitch: Float): PacketContainer {
		this.location.yaw = yaw
		this.location.pitch = pitch
		return createUpdateEntityPositionAndRotationPacket(location)
	}

	fun rotateHead(yaw: Float = this.location.yaw): PacketContainer {
		this.location.yaw = yaw
		return if (entityType.isAlive) createRotateHeadPacket(yaw) else createUpdateEntityPositionAndRotationPacket(
			location
		)
	}

	private fun createUpdateEntityPositionAndRotationPacket(location: Location): PacketContainer {
		val packet = PacketContainer(PacketType.Play.Server.REL_ENTITY_MOVE_LOOK)
		packet.integers.write(0, entityId)
		packet.shorts.write(0, this.location.x.calculateDelta(location.x))
		packet.shorts.write(1, this.location.y.calculateDelta(location.y))
		packet.shorts.write(2, this.location.z.calculateDelta(location.z))

		packet.bytes.write(0, location.yaw.convertRotation())
		packet.bytes.write(1, location.pitch.convertRotation())

		packet.booleans.write(0, false)
		return packet
	}

	private fun createTeleportEntityPacket(location: Location): PacketContainer {
		val packet = PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT)
		packet.integers.write(0, entityId)
		packet.doubles.write(0, location.x)
		packet.doubles.write(1, location.y)
		packet.doubles.write(2, location.z)
		packet.bytes.write(0, location.yaw.convertRotation())
		packet.bytes.write(1, location.pitch.convertRotation())
		return packet
	}

	fun createRotateHeadPacket(yaw: Float): PacketContainer {
		val packet = PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION)
		packet.integers.write(0, entityId)
		packet.bytes.write(0, yaw.convertRotation())
		return packet
	}
}

private fun Double.calculateDelta(new: Double): Short {
	return ((new * 32 - this * 32) * 128).toInt().toShort()
}