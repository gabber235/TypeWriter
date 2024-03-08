package me.gabber235.typewriter.entries.data.minecraft.living.scheep

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.passive.SheepMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("sheep_sheared_data", "If the sheep is sheared.", Colors.RED, "mdi:sheep")

class SheepShearedData (
    override val id: String = "",
    override val name: String = "",
    @Help("If the sheep is sheared.")
    val sheepSheared: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<ShearedProperty> {
    override val type: KClass<ShearedProperty> = ShearedProperty::class

    override fun build(player: Player): ShearedProperty = ShearedProperty(sheepSheared)
}

data class ShearedProperty(val sheepSheared: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<ShearedProperty>(ShearedProperty::class)
}

fun applySheepShearedData(entity: WrapperEntity, property: ShearedProperty) {
    entity.metas {
        meta<SheepMeta> { isSheared = property.sheepSheared }
        error("Could not apply SheepShearedData to ${entity.entityType} entity.")
    }
}