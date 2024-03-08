package me.gabber235.typewriter.entries.data.minecraft.living.pig

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.passive.PigMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("pig_saddle_data", "If the pig has a saddle.", Colors.RED, "mdi:pig")

class PigSaddleData (
    override val id: String = "",
    override val name: String = "",
    @Help("If the pig has a saddle.")
    val pigSaddle: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<SaddleProperty> {
    override val type: KClass<SaddleProperty> = SaddleProperty::class

    override fun build(player: Player): SaddleProperty = SaddleProperty(pigSaddle)
}

data class SaddleProperty(val pigSaddle: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<SaddleProperty>(SaddleProperty::class)
}

fun applyPigSaddleData(entity: WrapperEntity, property: SaddleProperty) {
    entity.metas {
        meta<PigMeta> { setHasSaddle(property.pigSaddle) }
        error("Could not apply PigSaddleData to ${entity.entityType} entity.")
    }
}