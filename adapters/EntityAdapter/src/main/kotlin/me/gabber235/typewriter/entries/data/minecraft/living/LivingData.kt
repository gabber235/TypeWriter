package me.gabber235.typewriter.entries.data.minecraft.living

import me.gabber235.typewriter.entry.entries.EntityProperty
import me.tofaa.entitylib.wrapper.WrapperEntity

fun applyLivingEntityData(entity: WrapperEntity, property: EntityProperty): Boolean {
    when (property) {
        is EquipmentProperty -> applyEquipmentData(entity, property)
        else -> return false
    }

    return true
}
