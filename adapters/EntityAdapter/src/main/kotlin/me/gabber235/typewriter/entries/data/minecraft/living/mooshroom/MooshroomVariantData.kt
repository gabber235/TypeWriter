package me.gabber235.typewriter.entries.data.minecraft.living.mooshroom

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.passive.MooshroomMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("Mooshroom_variant_data", "The variant of the Mooshroom", Colors.RED, "mdi:mushroom")

class MooshroomVariantData (
    override val id: String = "",
    override val name: String = "",
    @Help("The variant of the mooshroom.")
    val mooshroomVariant: MooshroomMeta.Variant = MooshroomMeta.Variant.RED,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<VariantProperty> {
    override val type: KClass<VariantProperty> = VariantProperty::class

    override fun build(player: Player): VariantProperty = VariantProperty(mooshroomVariant)
}

data class VariantProperty(val mooshroomVariant: MooshroomMeta.Variant) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<VariantProperty>(VariantProperty::class)
}

fun applyMooshroomVariantData(entity: WrapperEntity, property: VariantProperty) {
    entity.metas {
        meta<MooshroomMeta> { variant = property.mooshroomVariant }
        error("Could not apply MooshroomVariantData to ${entity.entityType} entity.")
    }
}