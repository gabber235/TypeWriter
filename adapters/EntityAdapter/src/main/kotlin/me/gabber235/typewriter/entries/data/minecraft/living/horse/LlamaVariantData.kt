package me.gabber235.typewriter.entries.data.minecraft.living.horse

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.LlamaMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("llama_variant_data", "The variant of the Llama.", Colors.RED, "mdi:llama")
@Tags("llama_data", "variant_data")
class LlamaVariantData(
    override val id: String = "",
    override val name: String = "",
    @Help("The variant of the Llama.")
    val variant: LlamaMeta.Variant = LlamaMeta.Variant.CREAMY,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<LlamaVariantProperty> {
    override fun type(): KClass<LlamaVariantProperty> = LlamaVariantProperty::class

    override fun build(player: Player): LlamaVariantProperty = LlamaVariantProperty(variant)
}

data class LlamaVariantProperty(val variant: LlamaMeta.Variant) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<LlamaVariantProperty>(LlamaVariantProperty::class)
}

fun applyLlamaVariantData(entity: WrapperEntity, property: LlamaVariantProperty) {
    entity.metas {
        meta<LlamaMeta> { variant = property.variant }
        error("Could not apply LlamaVariantData to ${entity.entityType} entity.")
    }
}