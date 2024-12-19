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

@Entry("eating_data", "If the entity is eating.", Colors.RED, "mdi:horse")
@Tags("eating_data", "horse_data")
class HorseEatingData(
    override val id: String = "",
    override val name: String = "",
    val eating: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<EatingProperty> {
    override fun type(): KClass<EatingProperty> = EatingProperty::class

    override fun build(player: Player): EatingProperty = EatingProperty(eating)
}

data class EatingProperty(val eating: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<EatingProperty>(EatingProperty::class, EatingProperty(false))
}

fun applyHorseEatingData(entity: WrapperEntity, property: EatingProperty) {
    entity.metas {
        meta<BaseHorseMeta> { isEating = property.eating }
        error("Could not apply BaseHorseEatingData to ${entity.entityType} entity.")
    }
}