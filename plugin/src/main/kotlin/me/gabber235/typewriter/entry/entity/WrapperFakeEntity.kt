package me.gabber235.typewriter.entry.entity

import com.github.retrooper.packetevents.protocol.entity.type.EntityType
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.toPacketLocation
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.spigot.SpigotEntityLibAPI
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.Location
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
            applyProperty(it)
        }
        entity.entityMeta.setNotifyAboutChanges(true)
    }

    abstract fun applyProperty(property: EntityProperty)

    override fun spawn(location: Location) {
        entity.spawn(location.toPacketLocation())
        entity.addViewer(player.uniqueId)
    }

    override fun addPassenger(entity: FakeEntity) {
        this.entity.addPassenger(entity.entityId)
    }

    override fun dispose() {
        entity.despawn()
        entity.remove()
    }
}