package me.gabber235.typewriter.entries.data.minecraft.display

import me.gabber235.typewriter.utils.Vector
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
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
    @Help("The scale vector.")
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