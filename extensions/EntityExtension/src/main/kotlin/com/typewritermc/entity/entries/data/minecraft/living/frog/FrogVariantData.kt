package com.typewritermc.entity.entries.data.minecraft.living.frog

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.FrogMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("frog_variant_data", "Frog Variant Data", Colors.GREEN, "icon-park-solid:frog")
@Tags("frog_data")
class FrogData(
    override val id: String = "",
    override val name: String = "",
    val frogVariant: FrogMeta.Variant = FrogMeta.Variant.TEMPERATE,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<FrogVariantProperty> {
    override fun type(): KClass<FrogVariantProperty> = FrogVariantProperty::class

    override fun build(player: Player): FrogVariantProperty = FrogVariantProperty(frogVariant)
}

data class FrogVariantProperty(val frogVariant: FrogMeta.Variant) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<FrogVariantProperty>(FrogVariantProperty::class)
}

fun applyFrogVariantData(entity: WrapperEntity, property: FrogVariantProperty) {
    entity.metas {
        meta<FrogMeta> { variant = property.frogVariant }
        error("Could not apply FrogVariantData to ${entity.entityType} entity.")
    }
}