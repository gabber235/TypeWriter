package me.gabber235.typewriter.entries.data.minecraft.living

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.monster.EndermanMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("enderman_screaming_data", "Enderman Screaming Data", Colors.RED, "game-icons:eye-monster")
@Tags("enderman_screaming_data")
class EndermanScreamingData(
    override val id: String = "",
    override val name: String = "",
    @Help("Whether the enderman is screaming or not.")
    val screaming: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),

    ) : EntityData<EndermanScreamingProperty> {
    override fun type(): KClass<EndermanScreamingProperty> = EndermanScreamingProperty::class

    override fun build(player: Player): EndermanScreamingProperty = EndermanScreamingProperty(screaming)
}

data class EndermanScreamingProperty(val screaming: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<EndermanScreamingProperty>(EndermanScreamingProperty::class)
}

fun applyEndermanScreamingData(entity: WrapperEntity, property: EndermanScreamingProperty) {
    entity.metas {
        meta<EndermanMeta> { isScreaming = property.screaming }
        error("Could not apply EndermanScreamingData to ${entity.entityType} entity.")
    }
}