package com.typewritermc.entity.entries.data.minecraft

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.entry.entries.GenericEntityData
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.EntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("glowing_effect_data", "If the entity is glowing", Colors.RED, "bi:lightbulb-fill")
@Tags("glowing_effect_data")
class GlowingEffectData(
    override val id: String = "",
    override val name: String = "",
    @Help("Whether the entity is glowing.")
    @Default("true")
    val glowing: Boolean = true,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<GlowingEffectProperty> {
    override fun type(): KClass<GlowingEffectProperty> = GlowingEffectProperty::class

    override fun build(player: Player): GlowingEffectProperty = GlowingEffectProperty(glowing)
}

data class GlowingEffectProperty(val glowing: Boolean = false) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<GlowingEffectProperty>(GlowingEffectProperty::class, GlowingEffectProperty(false))
}

fun applyGlowingEffectData(entity: WrapperEntity, property: GlowingEffectProperty) {
    entity.metas {
        meta<EntityMeta> { setHasGlowingEffect(property.glowing) }
        error("Could not apply GlowingEffectData to ${entity.entityType} entity.")
    }
}