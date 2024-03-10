package me.gabber235.typewriter.entries.data.minecraft.living.horse

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.BaseHorseMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("horse_eating_data", "If the horse is eating.", Colors.RED, "mdi:horse")

class HorseEatingData (
    override val id: String = "",
    override val name: String = "",
    @Help("If the horse is eating.")
    val horseEating: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<EatingProperty> {
    override val type: KClass<EatingProperty> = EatingProperty::class

    override fun build(player: Player): EatingProperty = EatingProperty(horseEating)
}

data class EatingProperty(val horseEating: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<EatingProperty>(EatingProperty::class)
}

fun applyHorseEatingData(entity: WrapperEntity, property: EatingProperty) {
    entity.metas {
        meta<BaseHorseMeta> { isEating = property.horseEating }
        error("Could not apply BaseHorseEatingData to ${entity.entityType} entity.")
    }
}