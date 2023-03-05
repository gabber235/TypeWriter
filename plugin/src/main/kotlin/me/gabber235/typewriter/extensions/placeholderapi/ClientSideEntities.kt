package me.gabber235.typewriter.extensions.placeholderapi

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*

fun Player.sendPacket(packet: PacketContainer) {
	ProtocolLibrary.getProtocolManager().sendServerPacket(this, packet)
}

fun Player.spawnClientSideEntity(
	entityId: Int,
	entityType: EntityType,
	location: Location,
	velocity: Vector = Vector(0.0, 0.0, 0.0)
) {
	val packet = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY)

	packet.integers.write(0, entityId)
	packet.uuiDs.write(0, UUID.randomUUID())
	packet.entityTypeModifier.write(0, entityType)

	packet.doubles.write(0, location.x).write(1, location.y).write(2, location.z)

	packet.integers.write(1, velocity.x.convertVelocity())
		.write(2, velocity.y.convertVelocity())
		.write(3, velocity.z.convertVelocity())



	packet.bytes
		.write(0, location.yaw.convertRotation())
		.write(1, location.pitch.convertRotation())

	sendPacket(packet)
}

fun Player.despawnClientSideEntity(entityId: Int) {
	val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)

	packet.intLists.write(0, listOf(entityId))

	sendPacket(packet)
}

fun Player.spectateEntity(entityId: Int?) {
	val packet = PacketContainer(PacketType.Play.Server.CAMERA)

	packet.integers.write(0, entityId ?: this.entityId)

	sendPacket(packet)
}

fun Player.teleportEntity(entityId: Int, location: Location) {
	val packet = PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT)

	packet.integers.write(0, entityId)

	packet.doubles.write(0, location.x).write(1, location.y).write(2, location.z)

	packet.bytes
		.write(0, location.yaw.convertRotation())
		.write(1, location.pitch.convertRotation())

	sendPacket(packet)
}

private fun Double.convertVelocity(): Int = (this * 8000).toInt()
private fun Float.convertRotation(): Byte = (this * 256.0f / 360.0f).toInt().toByte()