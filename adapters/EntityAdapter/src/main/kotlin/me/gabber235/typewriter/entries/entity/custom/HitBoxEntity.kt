package me.gabber235.typewriter.entries.entity.custom

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entries.entity.WrapperFakeEntity
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.meta
import me.gabber235.typewriter.utils.Sound
import me.gabber235.typewriter.utils.Vector
import me.tofaa.entitylib.meta.other.InteractionMeta
import org.bukkit.entity.Player

@Entry(
    "hit_box_definition",
    "A hit box for an entity to allow interaction with a different entity",
    Colors.ORANGE,
    "mdi:cube-outline"
)
/**
 * The `HitBoxDefinition` class is an entry that represents a hit box for an entity to allow interaction with a different entity.
 *
 * ## How could this be used?
 * This could be when using a display entity since they don't have a hit box to allow interaction with.
 */
class HitBoxDefinition(
    override val id: String = "",
    override val name: String = "",
    val baseEntity: Ref<EntityDefinitionEntry> = emptyRef(),
    @Help("The offset of the hit box relative to the base entity.")
    val offset: Vector = Vector.ZERO,
    @Help("The width of the hit box.")
    val width: Double = 1.0,
    @Help("The height of the hit box.")
    val height: Double = 1.0,
) : EntityDefinitionEntry {
    override val displayName: String get() = baseEntity.get()?.displayName ?: ""
    override val sound: Sound get() = baseEntity.get()?.sound ?: Sound.EMPTY
    override val data: List<Ref<EntityData<*>>> get() = baseEntity.get()?.data ?: emptyList()

    override fun create(player: Player): FakeEntity {
        val entity = baseEntity.get()?.create(player)
            ?: throw IllegalStateException("A base entity must be specified for entry $name ($id)")
        return HitBoxEntity(player, entity, offset, width, height)
    }
}

class HitBoxEntity(
    player: Player,
    private val baseEntity: FakeEntity,
    private val offset: Vector,
    width: Double,
    height: Double,
) : WrapperFakeEntity(EntityTypes.INTERACTION, player) {
    override val entityId: Int
        get() = baseEntity.entityId

    init {
        entity.meta<InteractionMeta> {
            this.width = width.toFloat()
            this.height = height.toFloat()
            isResponsive = true
        }
    }

    override fun applyProperties(properties: List<EntityProperty>) {
        entity.entityMeta.setNotifyAboutChanges(false)
        properties.forEach(this::applyProperty)
        entity.entityMeta.setNotifyAboutChanges(true)
        baseEntity.consumeProperties(properties)
    }

    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is LocationProperty -> {
                entity.teleport(property.add(offset).toPacketLocation())
            }
        }
    }

    override fun spawn(location: LocationProperty) {
        super.spawn(location.add(offset))
        baseEntity.spawn(location)
    }

    override fun addPassenger(entity: FakeEntity) {
        baseEntity.addPassenger(entity)
    }

    override fun removePassenger(entity: FakeEntity) {
        baseEntity.removePassenger(entity)
    }

    override fun contains(entityId: Int): Boolean {
        return super.contains(entityId) || baseEntity.contains(entityId)
    }

    override fun dispose() {
        super.dispose()
        baseEntity.dispose()
    }
}