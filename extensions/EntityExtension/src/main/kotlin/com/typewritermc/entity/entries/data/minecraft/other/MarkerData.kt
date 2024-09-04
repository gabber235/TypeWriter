package com.typewritermc.entity.entries.data.minecraft.other

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
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