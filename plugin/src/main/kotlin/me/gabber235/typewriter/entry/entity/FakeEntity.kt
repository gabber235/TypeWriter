package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityProperty
import org.bukkit.entity.Player
import kotlin.reflect.KClass
import kotlin.reflect.full.safeCast

interface EntityCreator {
    fun create(player: Player): FakeEntity
}

abstract class FakeEntity(
    protected val player: Player
) {
    protected val properties = mutableMapOf<KClass<*>, EntityProperty>()

    abstract val entityId: Int

    fun consumeProperties(vararg properties: EntityProperty) {
        consumeProperties(properties.toList())
    }

    fun consumeProperties(properties: List<EntityProperty>) {
        val changedProperties = properties.filter {
            this.properties[it::class] != it
        }
        if (changedProperties.isEmpty()) return

        applyProperties(changedProperties)
        changedProperties.forEach {
            this.properties[it::class] = it
        }
    }

    abstract fun applyProperties(properties: List<EntityProperty>)

    open fun tick() {}

    open fun spawn(location: LocationProperty) {
    }

    abstract fun addPassenger(entity: FakeEntity)
    abstract fun removePassenger(entity: FakeEntity)
    abstract operator fun contains(entityId: Int): Boolean
    open fun dispose() {
    }

    fun <P : EntityProperty> property(type: KClass<P>): P? {
        return type.safeCast(properties[type])
    }
}