package me.gabber235.typewriter.entries.data.minecraft.living

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.monster.piglin.PiglinMeta
import me.tofaa.entitylib.meta.types.LivingEntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("piglin_dancing_data", "Whether the piglin is dancing", Colors.RED, "streamline:travel-wayfinder-man-arm-raises-2-man-raise-arm-scaning-detect-posture-security")
@Tags("piglin_data", "piglin_dancing_data")

class PiglinDancingData(
    override val id: String = "",
    override val name: String = "",
    @Help("Whether the piglin is dancing.")
    val dancing: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<PiglinDancingProperty> {
    override fun type(): KClass<PiglinDancingProperty> = PiglinDancingProperty::class

    override fun build(player: Player): PiglinDancingProperty = PiglinDancingProperty(dancing)
}

data class PiglinDancingProperty(val dancing: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<PiglinDancingProperty>(PiglinDancingProperty::class)
}

fun applyPiglinDancingData(entity: WrapperEntity, property: PiglinDancingProperty) {
    entity.metas {
        meta<PiglinMeta> { isDancing = property.dancing }
        error("Could not apply PiglinDancingData to ${entity.entityType} entity.")
    }
}