package me.gabber235.typewriter.entries.data.minecraft

import me.gabber235.typewriter.entry.entries.EntityProperty
import me.tofaa.entitylib.wrapper.WrapperEntity


fun applyGenericEntityData(entity: WrapperEntity, property: EntityProperty): Boolean {
    when (property) {
        is OnFireProperty -> applyOnFireData(entity, property)
        is GlowingEffectProperty -> applyGlowingEffectData(entity, property)
        is PoseProperty -> applyPoseData(entity, property)
        is CustomNameProperty -> applyCustomNameData(entity, property)
        else -> return false
    }

    return true
}
