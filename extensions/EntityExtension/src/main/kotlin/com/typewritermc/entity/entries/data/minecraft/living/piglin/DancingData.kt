package com.typewritermc.entity.entries.data.minecraft.living.piglin

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
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
@Tags("data", "dancing_data", "piglin_data")
class DancingData(
    override val id: String = "",
    override val name: String = "",
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