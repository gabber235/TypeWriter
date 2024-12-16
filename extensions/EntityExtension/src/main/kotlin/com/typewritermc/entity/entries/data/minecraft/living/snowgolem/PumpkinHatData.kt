package com.typewritermc.entity.entries.data.minecraft.living.snowgolem

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.golem.SnowGolemMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("pumpkin_hat_data", "The pumpkin hat state of the snow golem", Colors.RED, "game-icons:pumpkin")
@Tags("pumpkin_hat_data", "snow_golem_data")
class PumpkinHatData(
    override val id: String = "",
    override val name: String = "",
    val hasPumpkinHat: Boolean = true,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<PumpkinHatProperty> {
    override fun type(): KClass<PumpkinHatProperty> = PumpkinHatProperty::class

    override fun build(player: Player): PumpkinHatProperty = PumpkinHatProperty(hasPumpkinHat)
}

data class PumpkinHatProperty(val hasPumpkinHat: Boolean) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<PumpkinHatProperty>(PumpkinHatProperty::class, PumpkinHatProperty(true))
}

fun applyPumpkinHatData(entity: WrapperEntity, property: PumpkinHatProperty) {
    entity.metas {
        meta<SnowGolemMeta> { isHasPumpkinHat = property.hasPumpkinHat }
        error("Could not apply PumpkinHatData to ${entity.entityType} entity.")
    }
}