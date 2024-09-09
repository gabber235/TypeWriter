package com.typewritermc.entity.entries.data.minecraft.display

import com.typewritermc.core.utils.point.Vector
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
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

@Entry("scale3d_data", "Scale of a Display.", Colors.RED, "mdi:resize")
@Tags("scale3d_data")
class Scale3DData(
    override val id: String = "",
    override val name: String = "",
    val scale: Vector = Vector(1.0, 1.0, 1.0),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<Scale3DProperty> {
    override fun type(): KClass<Scale3DProperty> = Scale3DProperty::class

    override fun build(player: Player): Scale3DProperty =
        Scale3DProperty(scale)
}

data class Scale3DProperty(val scale: Vector) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<Scale3DProperty>(Scale3DProperty::class, Scale3DProperty(Vector(1.0, 1.0, 1.0)))
}

fun applyScale3DData(entity: WrapperEntity, property: Scale3DProperty) {
    entity.metas {
        meta<AbstractDisplayMeta> { scale = property.scale.toPacketVector3f() }
        error("Could not apply ScaleData to ${entity.entityType} entity.")
    }
}