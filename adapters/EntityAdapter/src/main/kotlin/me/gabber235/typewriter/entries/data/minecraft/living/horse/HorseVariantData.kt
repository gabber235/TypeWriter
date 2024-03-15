package me.gabber235.typewriter.entries.data.minecraft.living.horse

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.HorseMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("horse_variant_dat", "The variant of the horse.", Colors.RED, "mdi:horse")

class HorseVariantData(
    override val id: String = "",
    override val name: String = "",
    @Help("The variant of the horse.")
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