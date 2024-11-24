package com.typewritermc.entity.entries.data.minecraft.living.armorstand

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

@Entry("arms_data", "An arms data", Colors.RED, "openmoji:stick-figure-with-arms-raised")
@Tags("arms_data", "armor_stand_data")
class ArmsData(
    override val id: String = "",
    override val name: String = "",
    val hasArms: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<ArmsProperty> {
    override fun type(): KClass<ArmsProperty> = ArmsProperty::class

    override fun build(player: Player): ArmsProperty = ArmsProperty(hasArms)
}

data class ArmsProperty(val hasArms: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<ArmsProperty>(ArmsProperty::class, ArmsProperty(false))
}

fun applyArmsData(entity: WrapperEntity, property: ArmsProperty) {
    entity.metas {
        meta<ArmorStandMeta> { isHasArms = property.hasArms }
        error("Could not apply ArmsData to ${entity.entityType} entity.")
    }
}