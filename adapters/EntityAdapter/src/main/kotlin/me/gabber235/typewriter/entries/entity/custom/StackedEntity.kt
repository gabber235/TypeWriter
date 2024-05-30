package me.gabber235.typewriter.entries.entity.custom

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityDefinitionEntry
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.utils.Sound
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
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @Help("The entities that will be stacked on top of each other. First entity will be the bottom entity.")
    val definitions: List<Ref<EntityDefinitionEntry>> = emptyList(),
) : EntityDefinitionEntry {
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

    override fun applyProperties(properties: List<EntityProperty>) {
        if (entities.isEmpty()) return
        val otherProperties = properties.filter { it !is LocationProperty }
        // Only the bottom entity will have the location
        entities.first().consumeProperties(properties)
        entities.asSequence().drop(1).forEach { it.consumeProperties(otherProperties) }
    }

    override fun spawn(location: LocationProperty) {
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