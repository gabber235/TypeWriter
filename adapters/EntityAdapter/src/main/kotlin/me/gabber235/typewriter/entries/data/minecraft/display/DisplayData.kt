package me.gabber235.typewriter.entries.data.minecraft.display

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.tofaa.entitylib.wrapper.WrapperEntity

@Tags("display_data")
interface DisplayEntityData<P : EntityProperty> : EntityData<P>

fun applyDisplayEntityData(entity: WrapperEntity, property: EntityProperty): Boolean {
    when (property) {
        is TranslationProperty -> applyTranslationData(entity, property)
        is BillboardConstraintProperty -> applyBillboardConstraintData(entity, property)
        else -> return false
    }
    return true
}