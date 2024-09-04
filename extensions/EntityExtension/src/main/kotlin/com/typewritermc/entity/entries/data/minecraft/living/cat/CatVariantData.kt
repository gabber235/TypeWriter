package com.typewritermc.entity.entries.data.minecraft.living.cat

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.tameable.CatMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("cat_variant_data", "The variant of a cat.", Colors.RED, "mdi:cat")
@Tags("cat_data", "cat_variant_data")
class CatVariantData (
    override val id: String = "",
    override val name: String = "",
    val catVariant: CatMeta.Variant = CatMeta.Variant.TABBY,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<CatVariantProperty> {
    override fun type(): KClass<CatVariantProperty> = CatVariantProperty::class

    override fun build(player: Player): CatVariantProperty = CatVariantProperty(catVariant)
}

data class CatVariantProperty(val catVariant: CatMeta.Variant) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<CatVariantProperty>(CatVariantProperty::class)
}

fun applyCatVariantData(entity: WrapperEntity, property: CatVariantProperty) {
    entity.metas {
        meta<CatMeta> { variant = property.catVariant }
        error("Could not apply CatVariantData to ${entity.entityType} entity.")
    }
}