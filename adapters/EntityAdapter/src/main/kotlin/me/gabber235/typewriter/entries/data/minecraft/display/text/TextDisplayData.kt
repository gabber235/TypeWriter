package me.gabber235.typewriter.entries.data.minecraft.display.text

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.tofaa.entitylib.wrapper.WrapperEntity

@Tags("text_display_data")
interface TextDisplayEntityData<P : EntityProperty> : EntityData<P>

fun applyTextDisplayEntityData(entity: WrapperEntity, property: EntityProperty): Boolean {
    when (property) {
        is BackgroundColorProperty -> applyBackgroundColorData(entity, property)
        else -> return false
    }
    return true
}
