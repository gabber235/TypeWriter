package me.gabber235.typewriter.entries.data.minecraft.living.horse

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.BaseHorseMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("rearing_data", "If the entity is rearing.", Colors.RED, "mdi:horse")
@Tags("rearing_data")
class RearingData(
    override val id: String = "",
    override val name: String = "",
    @Help("If the entity is rearing.")
    val rearing: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<RearingProperty> {
    override fun type(): KClass<RearingProperty> = RearingProperty::class

    override fun build(player: Player): RearingProperty = RearingProperty(rearing)
}

data class RearingProperty(val rearing: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<RearingProperty>(RearingProperty::class, RearingProperty(false))
}

fun applyRearingData(entity: WrapperEntity, property: RearingProperty) {
    entity.metas {
        meta<BaseHorseMeta> { isRearing = property.rearing }
        error("Could not apply HorseRearingData to ${entity.entityType} entity.")
    }
}