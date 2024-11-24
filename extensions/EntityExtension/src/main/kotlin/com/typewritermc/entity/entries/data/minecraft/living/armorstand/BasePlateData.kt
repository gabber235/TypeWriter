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

@Entry("baseplate_data", "An baseplate data", Colors.RED, "memory:tile-caution-thin")
@Tags("baseplate_data", "armor_stand_data")
class BaseplateData(
    override val id: String = "",
    override val name: String = "",
    val hasBaseplate: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<BaseplateProperty> {
    override fun type(): KClass<BaseplateProperty> = BaseplateProperty::class

    override fun build(player: Player): BaseplateProperty = BaseplateProperty(hasBaseplate)
}

data class BaseplateProperty(val hasBaseplate: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<BaseplateProperty>(BaseplateProperty::class, BaseplateProperty(false))
}

fun applyBaseplateData(entity: WrapperEntity, property: BaseplateProperty) {
    entity.metas {
        meta<ArmorStandMeta> { isHasNoBasePlate = !property.hasBaseplate }
        error("Could not apply BaseplateData to ${entity.entityType} entity.")
    }
}