package me.gabber235.typewriter.entries.data.minecraft.living.rabbit

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.passive.RabbitMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("rabbit_type_data", "The type of the rabbit", Colors.RED, "mdi:rabbit")

class RabbitTypeData (
    override val id: String = "",
    override val name: String = "",
    @Help("The type of the rabbit.")
    val rabbitType: RabbitMeta.Type = RabbitMeta.Type.BROWN,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<TypeProperty> {
    override fun type(): KClass<TypeProperty> = TypeProperty::class

    override fun build(player: Player): TypeProperty = TypeProperty(rabbitType)
}

data class TypeProperty(val rabbitType: RabbitMeta.Type) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<TypeProperty>(TypeProperty::class)
}

fun applyRabbitTypeData(entity: WrapperEntity, property: TypeProperty) {
    entity.metas {
        meta<RabbitMeta> { type = property.rabbitType }
        error("Could not apply RabbitTypeData to ${entity.entityType} entity.")
    }
}