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

@Entry("llama_carpet_color_data", "The color of the llama's carpet.", Colors.RED, "mdi:llama")
@Tags("llama_data", "carpet_color_data")
class LlamaCarpetColorData(
    override val id: String = "",
    override val name: String = "",
    @Help("The color of the llama's carpet.")
    val color: Int = 0,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<LlamaCarpetColorProperty> {
    override fun type(): KClass<LlamaCarpetColorProperty> = LlamaCarpetColorProperty::class

    override fun build(player: Player): LlamaCarpetColorProperty = LlamaCarpetColorProperty(color)
}

data class LlamaCarpetColorProperty(val color: Int) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<LlamaCarpetColorProperty>(LlamaCarpetColorProperty::class)
}

fun applyLlamaCarpetColorData(entity: WrapperEntity, property: LlamaCarpetColorProperty) {
    entity.metas {
        meta<LlamaMeta> { carpetColor = property.color }
        error("Could not apply LlamaCarpetColorData to ${entity.entityType} entity.")
    }
}