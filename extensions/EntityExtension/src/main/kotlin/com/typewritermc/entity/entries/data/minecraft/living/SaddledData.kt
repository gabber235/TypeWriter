package com.typewritermc.entity.entries.data.minecraft.living

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.BaseHorseMeta
import me.tofaa.entitylib.meta.mobs.passive.PigMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("saddled_data", "If the entity has a saddle.", Colors.RED, "game-icons:saddle")
@Tags("saddled_data", "horse_data", "pig_data")
class SaddledData(
    override val id: String = "",
    override val name: String = "",
    @Default("true")
    val saddled: Boolean = true,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<SaddledProperty> {
    override fun type(): KClass<SaddledProperty> = SaddledProperty::class

    override fun build(player: Player): SaddledProperty = SaddledProperty(saddled)
}

data class SaddledProperty(val saddled: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<SaddledProperty>(SaddledProperty::class, SaddledProperty(false))
}

fun applySaddledData(entity: WrapperEntity, property: SaddledProperty) {
    entity.metas {
        meta<BaseHorseMeta> { isSaddled = property.saddled }
        meta<PigMeta> { setHasSaddle(property.saddled) }
        error("Could not apply BaseHorseSaddledData to ${entity.entityType} entity.")
    }
}