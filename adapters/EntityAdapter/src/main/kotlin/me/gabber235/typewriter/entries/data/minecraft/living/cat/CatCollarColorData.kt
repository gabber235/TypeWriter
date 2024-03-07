package me.gabber235.typewriter.entries.data.minecraft.living.cat

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.extras.DyeColor
import me.tofaa.entitylib.meta.mobs.tameable.CatMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("cat_collar_color_data", "The color of the cat's collar", Colors.RED, "mdi:cat")

class CollarColorData (
    override val id: String = "",
    override val name: String = "",
    @Help("The color of the cat's collar.")
    val catCollarColor: DyeColor = DyeColor.RED,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<CollarColorProperty> {
    override val type: KClass<CollarColorProperty> = CollarColorProperty::class

    override fun build(player: Player): CollarColorProperty = CollarColorProperty(catCollarColor)
}

data class CollarColorProperty(val catCollarColor: DyeColor) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<CollarColorProperty>(CollarColorProperty::class)
}

fun applyCollarColorData(entity: WrapperEntity, property: CollarColorProperty) {
    entity.metas {
        meta<CatMeta> { collarColor = property.catCollarColor }
        error("Could not apply CatCollarColorData to ${entity.entityType} entity.")
    }
}