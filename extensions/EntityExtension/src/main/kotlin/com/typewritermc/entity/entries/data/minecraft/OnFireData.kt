package com.typewritermc.entity.entries.data.minecraft

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.entry.entries.GenericEntityData
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.EntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("on_fire_data", "If the entity is on fire", Colors.RED, "bi:fire")
class OnFireData(
    override val id: String = "",
    override val name: String = "",
    @Help("Whether the entity is on fire.")
    @Default("true")
    val onFire: Boolean = true,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<OnFireProperty> {
    override fun type(): KClass<OnFireProperty> = OnFireProperty::class

    override fun build(player: Player): OnFireProperty = OnFireProperty(onFire)
}

data class OnFireProperty(val onFire: Boolean = false) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<OnFireProperty>(OnFireProperty::class, OnFireProperty(false))
}

fun applyOnFireData(entity: WrapperEntity, property: OnFireProperty) {
    entity.metas {
        meta<EntityMeta> { isOnFire = property.onFire }
        error("Could not apply OnFireData to ${entity.entityType} entity.")
    }
}