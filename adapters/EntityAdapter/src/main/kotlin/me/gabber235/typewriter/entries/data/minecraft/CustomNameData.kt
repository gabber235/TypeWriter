package me.gabber235.typewriter.entries.data.minecraft

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.gabber235.typewriter.utils.asMini
import me.tofaa.entitylib.meta.EntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("custom_name_data", "The custom name of the entity", Colors.RED, "cbi:abc")

class CustomNameData(
    override val id: String = "",
    override val name: String = "",
    @Help("The custom name of the entity.")
    val customName: String = "",
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : GenericEntityData<CustomNameProperty> {
    override val type: KClass<CustomNameProperty> = CustomNameProperty::class

    override fun build(player: Player): CustomNameProperty = CustomNameProperty(customName)
}

data class CustomNameProperty(val customName: String) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<CustomNameProperty>(CustomNameProperty::class)
}

fun applyCustomNameData(entity: WrapperEntity, property: CustomNameProperty) {
    entity.metas {
        meta<EntityMeta> {
            if (property.customName.isEmpty()) {
                isCustomNameVisible = false
                customName = null
                return@meta
            }
            isCustomNameVisible = true
            customName = property.customName.asMini()
            println("Name column: ${property.customName}")
        }
        error("Could not apply CustomNameData to ${entity.entityType} entity.")
    }
}