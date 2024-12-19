package com.typewritermc.entity.entries.data.minecraft.living.horse

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.HorseMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("horse_variant_data", "The variant of the horse.", Colors.RED, "mdi:horse")
@Tags("horse_variant_data", "horse_data")
class HorseVariantData(
    override val id: String = "",
    override val name: String = "",
    val color: HorseMeta.Color = HorseMeta.Color.WHITE,
    val marking: HorseMeta.Marking = HorseMeta.Marking.NONE,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<HorseVariantProperty> {
    override fun type(): KClass<HorseVariantProperty> = HorseVariantProperty::class

    override fun build(player: Player): HorseVariantProperty = HorseVariantProperty(HorseMeta.Variant(marking, color))
}

data class HorseVariantProperty(val variant: HorseMeta.Variant) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<HorseVariantProperty>(HorseVariantProperty::class)
}

fun applyHorseVariantData(entity: WrapperEntity, property: HorseVariantProperty) {
    entity.metas {
        meta<HorseMeta> { variant = property.variant }
        error("Could not apply HorseVariantData to ${entity.entityType} entity.")
    }
}