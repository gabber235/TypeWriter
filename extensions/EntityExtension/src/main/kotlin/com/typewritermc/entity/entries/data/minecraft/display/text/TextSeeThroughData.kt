package com.typewritermc.entity.entries.data.minecraft.display.text

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.display.TextDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("text_see_through_data", "If a TextDisplay is see through.", Colors.RED, "mdi:texture")
@Tags("text_see_through_data")

class TextSeeThroughData(
    override val id: String = "",
    override val name: String = "",
    val seeThrough: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : TextDisplayEntityData<SeeThroughProperty> {
    override fun type(): KClass<SeeThroughProperty> = SeeThroughProperty::class

    override fun build(player: Player): SeeThroughProperty =
        SeeThroughProperty(seeThrough)
}

data class SeeThroughProperty(val seeThrough: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<SeeThroughProperty>(SeeThroughProperty::class, SeeThroughProperty(false))
}

fun applySeeThroughData(entity: WrapperEntity, property: SeeThroughProperty) {
    entity.metas {
        meta<TextDisplayMeta> { isSeeThrough = property.seeThrough }
        error("Could not apply SeeThroughData to ${entity.entityType} entity.")
    }
}
