package com.typewritermc.entity.entries.data.minecraft.display

import com.typewritermc.core.utils.point.Vector
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import com.typewritermc.engine.paper.utils.toPacketVector3f
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("scale_data", "Scale of a Display.", Colors.RED, "mdi:resize")
@Tags("scale_data")

class ScaleData(
    override val id: String = "",
    override val name: String = "",
    val scale: Vector = Vector(1.0, 1.0, 1.0),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<ScaleProperty> {
    override fun type(): KClass<ScaleProperty> = ScaleProperty::class

    override fun build(player: Player): ScaleProperty =
        ScaleProperty(scale)
}

data class ScaleProperty(val scale: Vector) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<ScaleProperty>(ScaleProperty::class, ScaleProperty(Vector(1.0, 1.0, 1.0)))
}

fun applyScaleData(entity: WrapperEntity, property: ScaleProperty) {
    entity.metas {
        meta<AbstractDisplayMeta> { scale = property.scale.toPacketVector3f() }
        error("Could not apply ScaleData to ${entity.entityType} entity.")
    }
}