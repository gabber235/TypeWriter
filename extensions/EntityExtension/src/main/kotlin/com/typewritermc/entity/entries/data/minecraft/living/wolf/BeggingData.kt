package com.typewritermc.entity.entries.data.minecraft.living.wolf

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.tameable.WolfMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("begging_data", "The begging state of the wolf", Colors.RED, "game-icons:sitting-dog")
@Tags("begging_data", "wolf_data")
class BeggingData(
    override val id: String = "",
    override val name: String = "",
    val begging: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<BeggingProperty> {
    override fun type(): KClass<BeggingProperty> = BeggingProperty::class

    override fun build(player: Player): BeggingProperty = BeggingProperty(begging)
}

data class BeggingProperty(val wolfBegging: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<BeggingProperty>(BeggingProperty::class, BeggingProperty(false))
}

fun applyBeggingData(entity: WrapperEntity, property: BeggingProperty) {
    entity.metas {
        meta<WolfMeta> { isBegging = property.wolfBegging }
        error("Could not apply WolfBeggingData to ${entity.entityType} entity.")
    }
}