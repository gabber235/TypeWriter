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

@Entry("horse_saddled_data", "If the horse has a saddle.", Colors.RED, "mdi:horse")
class BaseHorseSaddledData (
    override val id: String = "",
    override val name: String = "",
    @Help("If the horse has a saddle.")
    val horseSaddled: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<HorseSaddledProperty> {
    override val type: KClass<HorseSaddledProperty> = HorseSaddledProperty::class

    override fun build(player: Player): HorseSaddledProperty = HorseSaddledProperty(horseSaddled)
}

data class HorseSaddledProperty(val horseSaddled: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<HorseSaddledProperty>(HorseSaddledProperty::class)
}

fun applyBaseHorseSaddledData(entity: WrapperEntity, property: HorseSaddledProperty) {
    entity.metas {
        meta<BaseHorseMeta> { isSaddled = property.horseSaddled }
        error("Could not apply BaseHorseSaddledData to ${entity.entityType} entity.")
    }
}