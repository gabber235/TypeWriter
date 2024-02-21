package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityProperty
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.reflect.KClass

interface EntityCreator {
    fun create(player: Player): FakeEntity
}

abstract class FakeEntity(
    protected val player: Player
) {
    protected val properties = mutableMapOf<KClass<*>, EntityProperty>()

    abstract val entityId: Int

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

    abstract fun spawn(location: Location)
    abstract fun addPassenger(entity: FakeEntity)
    abstract fun dispose()
}