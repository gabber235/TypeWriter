package me.gabber235.typewriter.entries.entity

import com.github.retrooper.packetevents.protocol.entity.type.EntityType
import me.gabber235.typewriter.entries.entity.custom.state
import me.gabber235.typewriter.entry.entity.EntityState
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.meta.EntityMeta
import me.tofaa.entitylib.meta.projectile.ThrownExpBottleMeta
import me.tofaa.entitylib.meta.types.LivingEntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import me.tofaa.entitylib.wrapper.WrapperExperienceOrbEntity
import me.tofaa.entitylib.wrapper.WrapperLivingEntity
import org.bukkit.entity.Player

abstract class WrapperFakeEntity(
    val type: EntityType,
    player: Player,
) : FakeEntity(player) {
    protected val entity: WrapperEntity by lazy(LazyThreadSafetyMode.NONE) {
        val uuid = EntityLib.getPlatform().entityUuidProvider.provide(type)
        val entityId = EntityLib.getPlatform().entityIdProvider.provide(uuid, type)
        val metaData = EntityMeta.createMeta(entityId, type)
        when (metaData) {
            is LivingEntityMeta -> WrapperLivingEntity(entityId, uuid, type, metaData)
            is ThrownExpBottleMeta -> WrapperExperienceOrbEntity(entityId, uuid, type, metaData)
            else -> WrapperEntity(type)
        }
    }

    override val entityId: Int
        get() = entity.entityId

    override val state: EntityState
        get() = type.state(properties)

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

    override fun contains(entityId: Int): Boolean = entityId == entity.entityId

    override fun dispose() {
        super.dispose()
        entity.despawn()
        entity.remove()
    }
}