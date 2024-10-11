package com.typewritermc.entity.entries.entity.custom

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.utils.point.Vector
import com.typewritermc.engine.paper.entry.entity.FakeEntity
import com.typewritermc.engine.paper.entry.entity.PositionProperty
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityDefinitionEntry
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.meta
import com.typewritermc.engine.paper.utils.Sound
import com.typewritermc.engine.paper.utils.toPacketLocation
import com.typewritermc.entity.entries.entity.WrapperFakeEntity
import me.tofaa.entitylib.meta.other.InteractionMeta
import org.bukkit.entity.Player

@Entry(
    "interaction_entity_definition",
    "An entity that allows interaction, responding to player input",
    Colors.BLUE,
    "mdi:hand-pointing"
)
/**
 * The `InteractionEntityDefinition` class defines an entity that allows interaction with the player.
 * It listens to interaction events and performs actions based on those events.
 */
class InteractionEntityDefinition(
    override val id: String = "",
    override val name: String = "",
    val baseEntity: Ref<EntityDefinitionEntry> = emptyRef(),
    val offset: Vector = Vector.ZERO,
    val width: Double = 1.0,
    val height: Double = 1.0,
    val interactionMessage: String = "You interacted with the entity!"
) : EntityDefinitionEntry {

    override val displayName: String get() = baseEntity.get()?.displayName ?: ""
    override val sound: Sound get() = baseEntity.get()?.sound ?: Sound.EMPTY
    override val data: List<Ref<EntityData<*>>> get() = baseEntity.get()?.data ?: emptyList()

    override fun create(player: Player): FakeEntity {
        val entity = baseEntity.get()?.create(player)
            ?: throw IllegalStateException("A base entity must be specified for entry $name ($id)")
        return InteractionEntity(player, entity, offset, width, height, interactionMessage)
    }
}

class InteractionEntity(
    player: Player,
    private val baseEntity: FakeEntity,
    private val offset: Vector,
    width: Double,
    height: Double,
    private val interactionMessage: String
) : WrapperFakeEntity(EntityTypes.INTERACTION, player) {

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
            is PositionProperty -> {
                entity.teleport(property.add(offset).toPacketLocation())
            }
        }
    }

    override fun spawn(location: PositionProperty) {
        super.spawn(location.add(offset))
        baseEntity.spawn(location)
    }

    fun onPlayerInteract(player: Player) {
        player.sendMessage(interactionMessage)
    }

    override fun dispose() {
        super.dispose()
        baseEntity.dispose()
    }
}
