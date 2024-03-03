package me.gabber235.typewriter.entries.data.minecraft.display.item

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entries.data.minecraft.display.DisplayEntityData
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.gabber235.typewriter.utils.Item
import me.tofaa.entitylib.meta.display.ItemDisplayMeta
import me.tofaa.entitylib.meta.display.ItemDisplayMeta.DisplayType
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("Display_type_data", "Item for a display", Colors.RED, "mdi:tools")
@Tags("item_display_data")
class DisplayTypeData(
    override val id: String = "",
    override val name: String = "",
    @Help("Display Type")
    val display: DisplayType  = DisplayType.NONE ,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<DisplayTypeProperty> {
    override val type: KClass<DisplayTypeProperty> = DisplayTypeProperty::class

    override fun build(player: Player): DisplayTypeProperty = DisplayTypeProperty(display)
}

data class DisplayTypeProperty(val display: DisplayType) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<DisplayTypeProperty>(DisplayTypeProperty::class)
}

fun applyDisplayTypeData(entity: WrapperEntity, property: DisplayTypeProperty) {
    entity.metas {
        meta<ItemDisplayMeta> { displayType = property.display }
        error("Could not apply DisplayTypeData to ${entity.entityType} entity.")
    }
}