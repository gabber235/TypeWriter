package com.typewritermc.entity.entries.data.minecraft.living.tameable

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.BaseHorseMeta
import me.tofaa.entitylib.meta.types.TameableMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("tamed_data", "When a tamable entity is tamed", Colors.RED, "game-icons:sitting-dog")
@Tags("tamed_data", "horse_data")
class TamedData(
    override val id: String = "",
    override val name: String = "",
    @Default("true")
    val tamed: Boolean = true,
    override val priorityOverride: Optional<Int> = Optional.empty(),
    ) : TameableData<TamedProperty> {
    override fun type(): KClass<TamedProperty> = TamedProperty::class
    override fun build(player: Player): TamedProperty = TamedProperty(tamed)
}

data class TamedProperty(val tamed: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<TamedProperty>(TamedProperty::class, TamedProperty(false))
}

fun applyTamedData(entity: WrapperEntity, property: TamedProperty) {
    entity.metas {
        meta<TameableMeta> { isTamed = property.tamed }
        meta<BaseHorseMeta> { isTamed = property.tamed }
        error("Could not apply TamedData to ${entity.entityType} entity.")
    }
}