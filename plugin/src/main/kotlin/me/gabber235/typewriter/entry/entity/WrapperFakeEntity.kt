package me.gabber235.typewriter.entry.entity

import com.github.retrooper.packetevents.protocol.entity.type.EntityType
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.spigot.SpigotEntityLibAPI
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player

abstract class WrapperFakeEntity(
    type: EntityType,
    player: Player,
) : FakeEntity(player) {
    protected val entity = EntityLib.getApi<SpigotEntityLibAPI>().createEntity<WrapperEntity>(type)

    override val entityId: Int
        get() = entity.entityId

    override fun applyProperties(properties: List<EntityProperty>) {
        entity.entityMeta.setNotifyAboutChanges(false)
        properties.forEach {
            when (it) {
                is LocationProperty -> {
                    entity.teleport(it.toPacketLocation())
                    entity.rotateHead(it.yaw, it.pitch)
                }

                else -> applyProperty(it)
            }
        }
        entity.entityMeta.setNotifyAboutChanges(true)
    }

    abstract fun applyProperty(property: EntityProperty)

    override fun spawn(location: LocationProperty) {
        entity.spawn(location.toPacketLocation())
        entity.addViewer(player.uniqueId)
        super.spawn(location)
    }

    override fun addPassenger(entity: FakeEntity) {
        this.entity.addPassenger(entity.entityId)
    }

    override fun removePassenger(entity: FakeEntity) {
        this.entity.removePassenger(entity.entityId)
    }

    override fun contains(entityId: Int): Boolean = entityId == this.entityId

    override fun dispose() {
        super.dispose()
        entity.despawn()
        entity.remove()
    }
}