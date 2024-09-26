package com.typewritermc.entity.entries.data.minecraft.display.item

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import com.typewritermc.entity.entries.data.minecraft.display.DisplayEntityData
import me.tofaa.entitylib.meta.display.ItemDisplayMeta
import me.tofaa.entitylib.meta.display.ItemDisplayMeta.DisplayType
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("display_type_data", "Type of display for an ItemDisplay.", Colors.RED, "mdi:tools")
@Tags("item_display_data")
class DisplayTypeData(
    override val id: String = "",
    override val name: String = "",
    val display: DisplayType = DisplayType.NONE,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<DisplayTypeProperty> {
    override fun type(): KClass<DisplayTypeProperty> = DisplayTypeProperty::class

    override fun build(player: Player): DisplayTypeProperty = DisplayTypeProperty(display)
}

data class DisplayTypeProperty(val display: DisplayType) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<DisplayTypeProperty>(
        DisplayTypeProperty::class,
        DisplayTypeProperty(DisplayType.NONE)
    )
}

fun applyDisplayTypeData(entity: WrapperEntity, property: DisplayTypeProperty) {
    entity.metas {
        meta<ItemDisplayMeta> { displayType = property.display }
        error("Could not apply DisplayTypeData to ${entity.entityType} entity.")
    }
}