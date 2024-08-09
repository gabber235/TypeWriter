package me.gabber235.typewriter.entries.data.minecraft.living

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.BaseHorseMeta
import me.tofaa.entitylib.meta.mobs.passive.PigMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("saddled_data", "If the entity has a saddle.", Colors.RED, "game-icons:saddle")
@Tags("horse_data", "pig_data", "saddled_data")
class SaddledData (
    override val id: String = "",
    override val name: String = "",
    @Help("If the entity has a saddle.")
    val saddled: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<SaddledProperty> {
    override fun type(): KClass<SaddledProperty> = SaddledProperty::class

    override fun build(player: Player): SaddledProperty = SaddledProperty(saddled)
}

data class SaddledProperty(val saddled: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<SaddledProperty>(SaddledProperty::class, SaddledProperty(false))
}

fun applySaddledData(entity: WrapperEntity, property: SaddledProperty) {
    entity.metas {
        meta<BaseHorseMeta> { isSaddled = property.saddled }
        meta<PigMeta> { setHasSaddle(property.saddled) }
        error("Could not apply BaseHorseSaddledData to ${entity.entityType} entity.")
    }
}