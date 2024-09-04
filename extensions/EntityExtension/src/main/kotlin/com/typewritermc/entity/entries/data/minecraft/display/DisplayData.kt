package com.typewritermc.entity.entries.data.minecraft.display

import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import me.tofaa.entitylib.wrapper.WrapperEntity

@Tags("display_data")
interface DisplayEntityData<P : EntityProperty> : EntityData<P>

fun applyDisplayEntityData(entity: WrapperEntity, property: EntityProperty): Boolean {
    when (property) {
        is TranslationProperty -> applyTranslationData(entity, property)
        is BillboardConstraintProperty -> applyBillboardConstraintData(entity, property)
        is ScaleProperty -> applyScaleData(entity, property)
        else -> return false
    }
    return true
}