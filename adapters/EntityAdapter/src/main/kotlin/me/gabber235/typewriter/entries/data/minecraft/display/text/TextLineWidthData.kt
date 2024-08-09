package me.gabber235.typewriter.entries.data.minecraft.display.text

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.display.TextDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("text_line_width_data", "LineWidth for a TextDisplay.", Colors.RED, "mdi:format-letter-spacing")
@Tags("text_line_width_data")

class TextLineWidthData(
    override val id: String = "",
    override val name: String = "",
    @Help("LineWidth of the TextDisplay.")
    val lineWidth: Int = 0,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : TextDisplayEntityData<LineWidthProperty> {
    override fun type(): KClass<LineWidthProperty> = LineWidthProperty::class

    override fun build(player: Player): LineWidthProperty =
        LineWidthProperty(lineWidth)
}

data class LineWidthProperty(val lineWidth: Int) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<LineWidthProperty>(LineWidthProperty::class)
}

fun applyLineWidthData(entity: WrapperEntity, property: LineWidthProperty) {
    entity.metas {
        meta<TextDisplayMeta> { lineWidth = property.lineWidth }
        error("Could not apply LineWidthData to ${entity.entityType} entity.")
    }
}