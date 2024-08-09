package me.gabber235.typewriter.entries.data.minecraft.other

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.other.ArmorStandMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("marker_data", "Whether the entity is a marker", Colors.RED, "mdi:marker")
@Tags("marker_data", "armor_stand_data")
class MarkerData(
    override val id: String = "",
    override val name: String = "",
    @Help("Whether the entity is a marker.")
    val marker: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<MarkerProperty> {
    override fun type(): KClass<MarkerProperty> = MarkerProperty::class

    override fun build(player: Player): MarkerProperty = MarkerProperty(marker)
}

data class MarkerProperty(val marker: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<MarkerProperty>(MarkerProperty::class, MarkerProperty(false))
}

fun applyMarkerData(entity: WrapperEntity, property: MarkerProperty) {
    entity.metas {
        meta<ArmorStandMeta> { isMarker = property.marker }
        error("Could not apply MarkerData to ${entity.entityType} entity.")
    }
}