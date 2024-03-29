package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.PropertyCollector
import me.gabber235.typewriter.entry.entries.PropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.PropertySupplier
import org.bukkit.entity.Player
import kotlin.reflect.KClass

class SinglePropertyCollector<P : EntityProperty>(
    private val suppliers: List<PropertySupplier<out P>>,
    override val type: KClass<P>,
) : PropertyCollector<P> {
    override fun collect(player: Player): List<P> {
        val property = suppliers.firstOrNull { it.canApply(player) }?.build(player)
        return if (property != null) listOf(property) else emptyList()
    }
}

open class SinglePropertyCollectorSupplier<P : EntityProperty>(override val type: KClass<P>) :
    PropertyCollectorSupplier<P> {
    override fun collector(suppliers: List<PropertySupplier<out P>>): PropertyCollector<P> =
        SinglePropertyCollector(suppliers, type)
}