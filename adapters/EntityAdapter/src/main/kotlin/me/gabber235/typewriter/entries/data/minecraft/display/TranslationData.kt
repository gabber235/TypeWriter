package me.gabber235.typewriter.entries.data.minecraft.display

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.gabber235.typewriter.utils.Vector
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("translation_data", "Translate the entity", Colors.RED, "material-symbols:move-selection-up-rounded")
@Tags("translation_data")
class TranslationData(
    override val id: String = "",
    override val name: String = "",
    @Help("The translation vector.")
    val vector: Vector = Vector(0.0, 0.0, 0.0),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<TranslationProperty> {
    override val type: KClass<TranslationProperty> = TranslationProperty::class

    override fun build(player: Player): TranslationProperty = TranslationProperty(vector)
}

data class TranslationProperty(val vector: Vector) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<TranslationProperty>(TranslationProperty::class)
}

fun applyTranslationData(entity: WrapperEntity, property: TranslationProperty) {
    entity.metas {
        meta<AbstractDisplayMeta> { translation = property.vector.toPacketVector3f() }
        error("Could not apply TranslationData to ${entity.entityType} entity.")
    }
}