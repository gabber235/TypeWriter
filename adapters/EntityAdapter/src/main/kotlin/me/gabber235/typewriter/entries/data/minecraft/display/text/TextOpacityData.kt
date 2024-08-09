package me.gabber235.typewriter.entries.data.minecraft.display.text

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Max
import me.gabber235.typewriter.adapters.modifiers.Min
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.display.TextDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("text_opacity_data", "Opacity for a TextDisplay.", Colors.RED, "mdi:opacity")
@Tags("text_opacity_data")
class TextOpacityData(
    override val id: String = "",
    override val name: String = "",
    @Help("Opacity of the TextDisplay.")
    @Min(-1)
    @Max(127)
    val opacity: Int = -1,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : TextDisplayEntityData<TextOpacityProperty> {
    override fun type(): KClass<TextOpacityProperty> = TextOpacityProperty::class

    override fun build(player: Player): TextOpacityProperty =
        TextOpacityProperty(opacity.toByte())
}

data class TextOpacityProperty(val opacity: Byte) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<TextOpacityProperty>(TextOpacityProperty::class, TextOpacityProperty(-1))
}

fun applyTextOpacityData(entity: WrapperEntity, property: TextOpacityProperty) {
    entity.metas {
        meta<TextDisplayMeta> { textOpacity = property.opacity }
        error("Could not apply TextOpacityData to ${entity.entityType} entity.")
    }
}