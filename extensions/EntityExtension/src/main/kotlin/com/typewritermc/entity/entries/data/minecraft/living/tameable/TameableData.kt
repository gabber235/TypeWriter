package com.typewritermc.entity.entries.data.minecraft.living.tameable

import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import me.tofaa.entitylib.wrapper.WrapperEntity

@Tags("tameable_data")
interface TameableData<P : EntityProperty> : EntityData<P>

fun applyTameableData(entity: WrapperEntity, property: EntityProperty): Boolean {
    when (property) {
        is SittingProperty -> applySittingData(entity, property)
        is TamedProperty -> applyTamedData(entity, property)
        else -> return false
    }
    return true
}