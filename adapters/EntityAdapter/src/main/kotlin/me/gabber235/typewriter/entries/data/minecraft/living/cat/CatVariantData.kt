package me.gabber235.typewriter.entries.data.minecraft.living.cat

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
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
    @Help("The variant of the cat.")
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