package com.typewritermc.entity.entries.data.minecraft.living.horse

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
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