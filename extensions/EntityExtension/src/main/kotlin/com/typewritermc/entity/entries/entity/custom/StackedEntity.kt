package com.typewritermc.entity.entries.entity.custom

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.entity.EntityState
import com.typewritermc.engine.paper.entry.entity.FakeEntity
import com.typewritermc.engine.paper.entry.entity.PositionProperty
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityDefinitionEntry
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.utils.Sound
import org.bukkit.entity.Player

@Entry("stacked_entity_definition", "A stacking of entities", Colors.ORANGE, "ic:baseline-stacked-bar-chart")
/**
 * The `StackedEntityDefinition` class is an entry that represents a stacking of entities.
 *
 * :::caution
 * All properties will be shared between all entities.
 * Even if it is defined on only one entity.
 * :::
 *
 * Only the bottom entity will have the location of the `StackedEntity` instance.
 *
 * ## How could this be used?
 * This could be used to stack entities on top of each other.
 * Like having a hologram above a mob.
 */
class StackedEntityDefinition(
    override val id: String = "",
    override val name: String = "",
    @Help("The entities that will be stacked on top of each other. First entity will be the bottom entity.")
    val definitions: List<Ref<EntityDefinitionEntry>> = emptyList(),
) : EntityDefinitionEntry {
    override val displayName: String get() = definitions.firstOrNull()?.get()?.displayName ?: ""
    override val sound: Sound get() = definitions.firstOrNull()?.get()?.sound ?: Sound.EMPTY
    override val data: List<Ref<EntityData<*>>>
        get() = definitions.mapNotNull { it.get() }.flatMap { it.data }

    override fun create(player: Player): FakeEntity = StackedEntity(player, definitions.mapNotNull { it.get() })
}

class StackedEntity(
    player: Player,
    definitions: List<EntityDefinitionEntry>,
) : FakeEntity(player) {
    private val entities: List<FakeEntity> = definitions.map { it.create(player) }
    override val entityId: Int
        get() = entities.firstOrNull()?.entityId ?: -1

    override val state: EntityState
        get() = entities.firstOrNull()?.state ?: EntityState()

    override fun applyProperties(properties: List<EntityProperty>) {
        if (entities.isEmpty()) return
        val otherProperties = properties.filter { it !is PositionProperty }
        // Only the bottom entity will have the location
        entities.first().consumeProperties(properties)
        entities.asSequence().drop(1).forEach { it.consumeProperties(otherProperties) }
    }

    override fun spawn(location: PositionProperty) {
        if (entities.isEmpty()) return
        val baseEntity = entities.first()
        for (entity in entities) {
            entity.spawn(location)
            baseEntity.addPassenger(entity)
        }
    }

    override fun tick() {
        super.tick()
        entities.forEach { it.tick() }
    }

    override fun addPassenger(entity: FakeEntity) {
        entities.firstOrNull()?.addPassenger(entity)
    }

    override fun removePassenger(entity: FakeEntity) {
        entities.firstOrNull()?.removePassenger(entity)
    }

    override fun contains(entityId: Int): Boolean = entities.any { it.contains(entityId) }

    override fun dispose() {
        entities.forEach { it.dispose() }
    }
}