package com.typewritermc.entity.entries.data.minecraft.display.text

import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty

@Tags("text_display_data")
interface TextDisplayEntityData<P : EntityProperty> : EntityData<P>
