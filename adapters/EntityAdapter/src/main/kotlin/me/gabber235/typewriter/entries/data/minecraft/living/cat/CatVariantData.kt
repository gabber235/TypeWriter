package me.gabber235.typewriter.entries.data.minecraft.living.cat

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.tameable.CatMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("cat_variant_data", "The variant of the cat", Colors.RED, "mdi:cat")

class CatVariantData (
    override val id: String = "",
    override val name: String = "",
    @Help("The variant of the cat.")
    val catVariant: CatMeta.Variant = CatMeta.Variant.TABBY,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<VariantProperty> {
    override val type: KClass<VariantProperty> = VariantProperty::class

    override fun build(player: Player): VariantProperty = VariantProperty(catVariant)
}

data class VariantProperty(val catVariant: CatMeta.Variant) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<VariantProperty>(VariantProperty::class)
}

fun applyCatVariantData(entity: WrapperEntity, property: VariantProperty) {
    entity.metas {
        meta<CatMeta> { variant = property.catVariant }
        error("Could not apply CatVariantData to ${entity.entityType} entity.")
    }
}