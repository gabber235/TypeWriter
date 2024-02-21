package me.gabber235.typewriter.entries.data

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.monster.ZoglinMeta
import me.tofaa.entitylib.meta.mobs.monster.piglin.PiglinMeta
import me.tofaa.entitylib.meta.mobs.monster.zombie.ZombieMeta
import me.tofaa.entitylib.meta.types.AgeableMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("ageable_data", "An ageable data", Colors.RED, "material-symbols:child-care")
@Tags("ageable_data", "zoglin_data", "zombie_data")
class AgeableData(
    override val id: String = "",
    override val name: String = "",
    @Help("Whether the entity is a baby.")
    val baby: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<AgableProperty> {
    override val type: KClass<AgableProperty> = AgableProperty::class

    override fun build(player: Player): AgableProperty = AgableProperty(baby)
}

data class AgableProperty(val baby: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<AgableProperty>(AgableProperty::class)
}

fun applyAgeableData(entity: WrapperEntity, property: AgableProperty) {
    entity.metas {
        meta<AgeableMeta> { isBaby = property.baby }
        meta<ZoglinMeta> { isBaby = property.baby }
        meta<ZombieMeta> { isBaby = property.baby }
        meta<PiglinMeta> { isBaby = property.baby }
        error("Could not apply AgeableData to ${entity.entityType} entity.")
    }
}