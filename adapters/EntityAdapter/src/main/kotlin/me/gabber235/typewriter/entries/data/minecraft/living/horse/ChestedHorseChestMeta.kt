package me.gabber235.typewriter.entries.data.minecraft.living.horse

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.GenericEntityData
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.ChestedHorseMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("chested_horse_chest_meta", "If the horse has a chest.", Colors.RED, "mdi:horse")
@Tags("chested_horse_data", "chested_horse_chest_meta")
class ChestedHorseChestData (
    override val id: String = "",
    override val name: String = "",
    @Help("If the horse has a chest.")
    val chestedHorse: Boolean = false,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<ChestedHorseChestProperty> {
    override fun type(): KClass<ChestedHorseChestProperty> = ChestedHorseChestProperty::class

    override fun build(player: Player): ChestedHorseChestProperty = ChestedHorseChestProperty(chestedHorse)
}

data class ChestedHorseChestProperty(val chestedHorse: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<ChestedHorseChestProperty>(ChestedHorseChestProperty::class)
}

fun applyChestedHorseChestData(entity: WrapperEntity, property: ChestedHorseChestProperty) {
    entity.metas {
        meta<ChestedHorseMeta> { isHasChest = property.chestedHorse }
        error("Could not apply ChestedHorseChestData to ${entity.entityType} entity.")
    }
}