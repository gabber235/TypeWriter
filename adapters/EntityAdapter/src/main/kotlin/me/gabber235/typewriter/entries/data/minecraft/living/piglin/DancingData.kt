package me.gabber235.typewriter.entries.data.minecraft.living.piglin

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.monster.piglin.PiglinMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry(
    "dancing_data",
    "Whether an entity is dancing",
    Colors.RED,
    "streamline:travel-wayfinder-man-arm-raises-2-man-raise-arm-scaning-detect-posture-security"
)
@Tags("data", "dancing_data")
class DancingData(
    override val id: String = "",
    override val name: String = "",
    @Help("Whether the piglin is dancing.")
    val dancing: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<DancingProperty> {
    override fun type(): KClass<DancingProperty> = DancingProperty::class

    override fun build(player: Player): DancingProperty = DancingProperty(dancing)
}

data class DancingProperty(val dancing: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<DancingProperty>(DancingProperty::class, DancingProperty(false))
}

fun applyDancingData(entity: WrapperEntity, property: DancingProperty) {
    entity.metas {
        meta<PiglinMeta> { isDancing = property.dancing }
        error("Could not apply DancingData to ${entity.entityType} entity.")
    }
}