package com.typewritermc.entity.entries.data.minecraft.living.parrot

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.tameable.ParrotMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("parrot_color_data", "The color of the parrot", Colors.RED, "ph:bird-fill")
@Tags("parrot_color_data", "parrot_data")
class ParrotColorData(
    override val id: String = "",
    override val name: String = "",
    val parrotColor: ParrotMeta.Color = ParrotMeta.Color.RED_BLUE,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<ParrotColorProperty> {
    override fun type(): KClass<ParrotColorProperty> = ParrotColorProperty::class

    override fun build(player: Player): ParrotColorProperty = ParrotColorProperty(parrotColor)
}

data class ParrotColorProperty(val parrotColor: ParrotMeta.Color) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<ParrotColorProperty>(ParrotColorProperty::class)
}

fun applyParrotColorData(entity: WrapperEntity, property: ParrotColorProperty) {
    entity.metas {
        meta<ParrotMeta> { color = property.parrotColor }
        error("Could not apply ParrotColorData to ${entity.entityType} entity.")
    }
}