package com.typewritermc.entity.entries.data.minecraft.living.horse

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.horse.ChestedHorseMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("chested_horse_chest_meta", "If the horse has a chest.", Colors.RED, "mdi:horse")
@Tags("chested_horse_chest_meta", "chested_horse_meta")
class ChestedHorseChestData(
    override val id: String = "",
    override val name: String = "",
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