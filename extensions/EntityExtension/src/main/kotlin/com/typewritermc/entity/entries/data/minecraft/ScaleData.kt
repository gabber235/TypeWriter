package com.typewritermc.entity.entries.data.minecraft

import com.github.retrooper.packetevents.protocol.attribute.Attributes
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.entry.entries.GenericEntityData
import me.tofaa.entitylib.wrapper.WrapperEntity
import me.tofaa.entitylib.wrapper.WrapperLivingEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("scale_data", "Scale of an Entity", Colors.RED, "fa6-solid:scale-balanced")
@Tags("scale_data")
class ScaleData(
    override val id: String = "",
    override val name: String = "",
    override val priorityOverride: Optional<Int> = Optional.empty(),
    @Default("1.0")
    val scale: Double = 1.0,
) : GenericEntityData<ScaleProperty> {
    override fun type(): KClass<ScaleProperty> = ScaleProperty::class
    override fun build(player: Player): ScaleProperty = ScaleProperty(scale)
}

data class ScaleProperty(val scale: Double) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<ScaleProperty>(ScaleProperty::class, ScaleProperty(1.0))
}

fun applyScaleData(entity: WrapperEntity, property: ScaleProperty) {
    // TODO: Remove this when EntityLib supports scale for all entities
    if (entity !is WrapperLivingEntity) return
    entity.attributes.setAttribute(Attributes.GENERIC_SCALE, property.scale)
}