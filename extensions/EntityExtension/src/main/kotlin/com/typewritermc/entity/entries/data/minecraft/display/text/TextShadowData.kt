package com.typewritermc.entity.entries.data.minecraft.display.text

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.display.TextDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("text_shadow_data", "If text in TextDisplay has shadow.", Colors.RED, "mdi:box-shadow")
@Tags("text_shadow_data")
class TextShadowData(
    override val id: String = "",
    override val name: String = "",
    val shadow: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : TextDisplayEntityData<ShadowProperty> {
    override fun type(): KClass<ShadowProperty> =
        ShadowProperty::class

    override fun build(player: Player): ShadowProperty =
        ShadowProperty(shadow)
}

data class ShadowProperty(val shadow: Boolean) : EntityProperty {
    companion object :
        SinglePropertyCollectorSupplier<ShadowProperty>(
            ShadowProperty::class,
            ShadowProperty(false)
        )
}

fun applyShadowData(
    entity: WrapperEntity,
    property: ShadowProperty
) {
    entity.metas {
        meta<TextDisplayMeta> { isShadow = property.shadow }
        error("Could not apply ShadowData to ${entity.entityType} entity.")
    }
}