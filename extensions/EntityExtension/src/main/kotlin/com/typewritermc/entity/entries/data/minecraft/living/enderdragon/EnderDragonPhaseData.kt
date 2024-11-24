package com.typewritermc.entity.entries.data.minecraft.living.enderdragon

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.other.EnderDragonMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("ender_dragon_phase_data", "Ender Dragon Phase Data", Colors.RED, "fa6-solid:dragon")
@Tags("ender_dragon_phase_data", "ender_dragon_data")
class EnderDragonPhaseData(
    override val id: String = "",
    override val name: String = "",
    val phase: EnderDragonMeta.Phase = EnderDragonMeta.Phase.CIRCLING,
    override val priorityOverride: Optional<Int> = Optional.empty()
) : EntityData<EnderDragonPhaseProperty> {
    override fun type(): KClass<EnderDragonPhaseProperty> = EnderDragonPhaseProperty::class

    override fun build(player: Player): EnderDragonPhaseProperty = EnderDragonPhaseProperty(phase)
}

data class EnderDragonPhaseProperty(val phase: EnderDragonMeta.Phase) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<EnderDragonPhaseProperty>(EnderDragonPhaseProperty::class, EnderDragonPhaseProperty(EnderDragonMeta.Phase.CIRCLING))
}

fun applyEnderDragonPhaseData(entity: WrapperEntity, property: EnderDragonPhaseProperty) {
    entity.metas {
        meta<EnderDragonMeta> { phase = property.phase }
        error("Could not apply EnderDragonPhaseData to ${entity.entityType} entity.")
    }
}