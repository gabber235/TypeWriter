package me.gabber235.typewriter.entries.data.minecraft.living

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.types.LivingEntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("arrow_count_data", "The amount of arrows in the entity", Colors.RED, "mdi:arrow-projectile")
class ArrowCountData(
    override val id: String = "",
    override val name: String = "",
    @Help("The amount of arrows in the entity.")
    val arrowCount: Int = 0,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<ArrowCountProperty> {
    override val type: KClass<ArrowCountProperty> = ArrowCountProperty::class

    override fun build(player: Player): ArrowCountProperty = ArrowCountProperty(arrowCount)
}

data class ArrowCountProperty(val arrowCount: Int) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<ArrowCountProperty>(ArrowCountProperty::class)
}

fun applyArrowCountData(entity: WrapperEntity, property: ArrowCountProperty) {
    entity.metas {
        meta<LivingEntityMeta> { arrowCount = property.arrowCount }
        error("Could not apply ArrowCountData to ${entity.entityType} entity.")
    }
}