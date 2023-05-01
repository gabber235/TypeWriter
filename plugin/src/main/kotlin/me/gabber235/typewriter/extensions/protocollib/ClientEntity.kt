package me.gabber235.typewriter.extensions.protocollib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import lirand.api.dsl.command.builders.command
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector
import java.util.*


private var testEntity: ClientEntity? = null
private var attribute: AttributeModifier? = null

fun Plugin.entityTestCommand() = command("test") {
    literal("create") {
        executesPlayer {
            val entity = ClientEntity(source.location, EntityType.HORSE)
            entity.addViewer(source.uniqueId)
            testEntity = entity
        }
    }

    literal("move") {
        executesPlayer {
            testEntity?.move(source.location)
        }
    }

    literal("glow") {
        executesPlayer {
            testEntity?.glowing = testEntity?.glowing != true
        }
    }

    literal("invisible") {
        executesPlayer {
            testEntity?.invisible = testEntity?.invisible != true
        }
    }

    literal("destroy") {
        executesPlayer {
            testEntity?.removeViewer(source.uniqueId)
            testEntity = null
        }
    }

    literal("zoom") {
        executesPlayer {
            val instance = source.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) ?: return@executesPlayer
            attribute = if (attribute == null) {
                val modifier = AttributeModifier("test_zoom", 1.0, Operation.MULTIPLY_SCALAR_1)
                instance.addModifier(modifier)
                modifier
            } else {
                instance.modifiers.forEach { instance.removeModifier(it) }
                null
            }
        }
    }
}

class ClientEntity(location: Location, private val entityType: EntityType) {
    private val entityId: Int = (Math.random() * 1000000 + 1000000).toInt()
    val uuid: UUID = UUID.randomUUID()

    private val viewers = ViewersComponent()
    private val metaDataComponent = EntityMetaDataComponent(entityId)
    private val positionComponent = PositionComponent(entityId, entityType, location, Vector())
    private val mountComponent = MountComponent(entityId)

    fun addViewer(player: Player) = addViewer(player.uniqueId)
    fun addViewer(uuid: UUID) {
        viewers.addViewer(uuid)
        viewers.sendPacket(uuid, createSpawnPacket())
        viewers.sendPacket(uuid, metaDataComponent.createEntityMetaDataPacket())
        positionComponent.createShowPackets().forEach { viewers.sendPacket(uuid, it) }
    }

    fun removeViewer(player: Player) = removeViewer(player.uniqueId)
    fun removeViewer(uuid: UUID) {
        viewers.sendPacket(uuid, createDestroyPacket())
        viewers.removeViewer(uuid)
    }

    fun move(location: Location) {
        viewers.sendPacket(positionComponent.move(location))
    }

    fun rotateHead(yaw: Float? = null) {
        if (yaw == null) {
            viewers.sendPacket(positionComponent.rotateHead())
            return
        }
        viewers.sendPacket(positionComponent.rotateHead(yaw))
    }

    fun spectateThis(player: Player) = spectateThis(player.uniqueId)
    fun spectateThis(uuid: UUID) {
        viewers.sendPacket(uuid, createSetCameraPacket())
    }

    fun mount(player: Player) {
        viewers.sendPacket(mountComponent.mount(player.entityId))
    }

    fun unmount(player: Player) {
        viewers.sendPacket(mountComponent.unmount(player.entityId))
    }

    var glowing: Boolean
        get() = metaDataComponent.hasStatus(EntityStatus.GLOWING)
        set(value) {
            if (value) {
                metaDataComponent.addStatus(EntityStatus.GLOWING)
            } else {
                metaDataComponent.removeStatus(EntityStatus.GLOWING)
            }
            viewers.sendPacket(metaDataComponent.createEntityMetaDataPacket())
        }

    var invisible: Boolean
        get() = metaDataComponent.hasStatus(EntityStatus.INVISIBLE)
        set(value) {
            if (value) {
                metaDataComponent.addStatus(EntityStatus.INVISIBLE)
            } else {
                metaDataComponent.removeStatus(EntityStatus.INVISIBLE)
            }
            viewers.sendPacket(metaDataComponent.createEntityMetaDataPacket())
        }

    private fun createSpawnPacket(): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.SPAWN_ENTITY)

        packet.integers.write(0, entityId)
        packet.uuiDs.write(0, uuid)
        packet.entityTypeModifier.write(0, entityType)

        packet.doubles.write(0, positionComponent.location.x).write(1, positionComponent.location.y)
            .write(2, positionComponent.location.z)

        packet.integers.write(1, positionComponent.velocity.x.convertVelocity())
            .write(2, positionComponent.velocity.y.convertVelocity())
            .write(3, positionComponent.velocity.z.convertVelocity())

        packet.bytes
            .write(0, positionComponent.location.yaw.convertRotation())
            .write(1, positionComponent.location.pitch.convertRotation())

        return packet
    }

    private fun createDestroyPacket(): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.ENTITY_DESTROY)
        packet.intLists.write(0, listOf(entityId))
        return packet
    }

    private fun createSetCameraPacket(): PacketContainer {
        val packet = PacketContainer(PacketType.Play.Server.CAMERA)
        packet.integers.write(0, entityId)
        return packet
    }
}

fun Double.convertVelocity(): Int = (this * 8000).toInt()
fun Float.convertRotation(): Byte = (this * 256.0f / 360.0f).toInt().toByte()

fun Player.spectateEntity(entity: ClientEntity) {
    entity.spectateThis(this)
}

fun Player.stopSpectatingEntity() {
    val packet = PacketContainer(PacketType.Play.Server.CAMERA)
    packet.integers.write(0, entityId)
    ProtocolLibrary.getProtocolManager().sendServerPacket(this, packet, false)
}

fun Player.mount(entity: ClientEntity) {
    entity.mount(this)
}

fun Player.unmount(entity: ClientEntity) {
    entity.unmount(this)
}