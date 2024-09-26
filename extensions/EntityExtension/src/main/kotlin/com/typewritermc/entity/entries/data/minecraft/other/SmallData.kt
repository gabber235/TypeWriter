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

@Entry("small_data", "Whether the entity is small", Colors.RED, "streamline:arrow-shrink-diagonal-1-solid")
@Tags("small_data", "armor_stand_data")
class SmallData(
    override val id: String = "",
    override val name: String = "",
    val small: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<SmallProperty> {
    override fun type(): KClass<SmallProperty> = SmallProperty::class

    override fun build(player: Player): SmallProperty = SmallProperty(small)
}

data class SmallProperty(val small: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<SmallProperty>(SmallProperty::class, SmallProperty(false))
}

fun applySmallData(entity: WrapperEntity, property: SmallProperty) {
    entity.metas {
        meta<ArmorStandMeta> { isSmall = property.small }
        error("Could not apply SmallData to ${entity.entityType} entity.")
    }
}