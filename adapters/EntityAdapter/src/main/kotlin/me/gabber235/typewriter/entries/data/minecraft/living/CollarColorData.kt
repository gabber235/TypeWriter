package me.gabber235.typewriter.entries.data.minecraft.living

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.extras.DyeColor
import me.tofaa.entitylib.meta.mobs.tameable.CatMeta
import me.tofaa.entitylib.meta.mobs.tameable.WolfMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("collar_color_data", "The color of the cat's or wolfs collar", Colors.RED, "fluent:paint-bucket-16-filled")
@Tags("cat_data", "wolf_data", "collar_color_data")
class CollarColorData(
    override val id: String = "",
    override val name: String = "",
    @Help("The color of the cat's collar.")
    val catCollarColor: DyeColor = DyeColor.RED,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<CollarColorProperty> {
    override fun type(): KClass<CollarColorProperty> = CollarColorProperty::class

    override fun build(player: Player): CollarColorProperty = CollarColorProperty(catCollarColor)
}

data class CollarColorProperty(val collarColor: DyeColor) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<CollarColorProperty>(CollarColorProperty::class)
}

fun applyCollarColorData(entity: WrapperEntity, property: CollarColorProperty) {
    entity.metas {
        meta<CatMeta> { collarColor = property.collarColor }
        meta<WolfMeta> { collarColor = property.collarColor.ordinal }
        error("Could not apply CatCollarColorData to ${entity.entityType} entity.")
    }
}