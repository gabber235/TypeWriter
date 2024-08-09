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

@Entry("small_data", "Whether the entity is small", Colors.RED, "mdi:small")
@Tags("small_data", "armor_stand_data")
class SmallData(
    override val id: String = "",
    override val name: String = "",
    @Help("Whether the entity is small.")
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