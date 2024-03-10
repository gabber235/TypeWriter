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

@Entry("horse_rearing_data", "If the horse is rearing.", Colors.RED, "mdi:horse")

class HorseRearingData (
    override val id: String = "",
    override val name: String = "",
    @Help("If the horse is rearing.")
    val horseRearing: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<HorseRearingProperty> {
    override val type: KClass<HorseRearingProperty> = HorseRearingProperty::class

    override fun build(player: Player): HorseRearingProperty = HorseRearingProperty(horseRearing)
}

data class HorseRearingProperty(val horseRearing: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<HorseRearingProperty>(HorseRearingProperty::class)
}

fun applyHorseRearingData(entity: WrapperEntity, property: HorseRearingProperty) {
    entity.metas {
        meta<BaseHorseMeta> { isRearing = property.horseRearing }
        error("Could not apply HorseRearingData to ${entity.entityType} entity.")
    }
}