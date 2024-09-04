package com.typewritermc.entity.entries.data.minecraft.living.rabbit

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.passive.RabbitMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("rabbit_type_data", "The type of the rabbit", Colors.RED, "mdi:rabbit")
@Tags("rabbit_data", "rabbit_type_data")
class RabbitTypeData(
    override val id: String = "",
    override val name: String = "",
    val rabbitType: RabbitMeta.Type = RabbitMeta.Type.BROWN,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<RabbitTypeProperty> {
    override fun type(): KClass<RabbitTypeProperty> = RabbitTypeProperty::class

    override fun build(player: Player): RabbitTypeProperty = RabbitTypeProperty(rabbitType)
}

data class RabbitTypeProperty(val rabbitType: RabbitMeta.Type) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<RabbitTypeProperty>(RabbitTypeProperty::class)
}

fun applyRabbitTypeData(entity: WrapperEntity, property: RabbitTypeProperty) {
    entity.metas {
        meta<RabbitMeta> { type = property.rabbitType }
        error("Could not apply RabbitTypeData to ${entity.entityType} entity.")
    }
}