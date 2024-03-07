package me.gabber235.typewriter.entries.data.minecraft.living.wolf

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.tameable.WolfMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("wolf_collar_color_data", "The color of the wolf's collar", Colors.RED, "mdi:dog")

class WolfCollarColorData (
    override val id: String = "",
    override val name: String = "",
    @Help("The color of the wolf's collar.")
    val wolfCollarColor: Int = 1,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<CollarColorProperty> {
    override val type: KClass<CollarColorProperty> = CollarColorProperty::class

    override fun build(player: Player): CollarColorProperty = CollarColorProperty(wolfCollarColor)
}

data class CollarColorProperty(val wolfCollarColor: Int) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<CollarColorProperty>(CollarColorProperty::class)
}

fun applyCollarColorData(entity: WrapperEntity, property: CollarColorProperty) {
    entity.metas {
        meta<WolfMeta> { collarColor = property.wolfCollarColor }
        error("Could not apply WolfCollarColorData to ${entity.entityType} entity.")
    }
}