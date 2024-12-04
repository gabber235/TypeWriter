package com.typewritermc.entity.entries.data.minecraft.display

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.core.utils.point.Vector
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.extensions.packetevents.metas
import com.typewritermc.engine.paper.utils.toPacketVector3f
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("translation_data", "Translate the Display.", Colors.RED, "material-symbols:move-selection-up-rounded")
@Tags("translation_data")
class TranslationData(
    override val id: String = "",
    override val name: String = "",
    val vector: Var<Vector> = ConstVar(Vector.ZERO),
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<TranslationProperty> {
    override fun type(): KClass<TranslationProperty> = TranslationProperty::class

    override fun build(player: Player): TranslationProperty = TranslationProperty(vector.get(player))
}

data class TranslationProperty(val vector: Vector) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<TranslationProperty>(
        TranslationProperty::class,
        TranslationProperty(Vector(0.0, 0.0, 0.0))
    )
}

fun applyTranslationData(entity: WrapperEntity, property: TranslationProperty) {
    entity.metas {
        meta<AbstractDisplayMeta> { translation = property.vector.toPacketVector3f() }
        error("Could not apply TranslationData to ${entity.entityType} entity.")
    }
}