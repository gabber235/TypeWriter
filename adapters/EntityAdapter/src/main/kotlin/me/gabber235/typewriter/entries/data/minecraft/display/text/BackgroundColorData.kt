package me.gabber235.typewriter.entries.data.minecraft.display.text

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.gabber235.typewriter.utils.Color
import me.tofaa.entitylib.meta.display.TextDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry(
    "background_color_data",
    "Background color for a text display",
    Colors.RED,
    "fluent:video-background-effect-32-filled"
)
@Tags("background_color_data")
class BackgroundColorData(
    override val id: String = "",
    override val name: String = "",
    @Help("Background Color")
    val color: Color = Color.BLACK_BACKGROUND,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : TextDisplayEntityData<BackgroundColorProperty> {
    override val type: KClass<BackgroundColorProperty> = BackgroundColorProperty::class

    override fun build(player: Player): BackgroundColorProperty =
        BackgroundColorProperty(color)
}

data class BackgroundColorProperty(val color: Color) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<BackgroundColorProperty>(BackgroundColorProperty::class)
}

fun applyBackgroundColorData(entity: WrapperEntity, property: BackgroundColorProperty) {
    entity.metas {
        meta<TextDisplayMeta> { backgroundColor = property.color.color }
        error("Could not apply BackgroundColorData to ${entity.entityType} entity.")
    }
}
