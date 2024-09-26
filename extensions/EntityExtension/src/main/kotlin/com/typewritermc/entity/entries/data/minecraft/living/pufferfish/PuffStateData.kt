package com.typewritermc.entity.entries.data.minecraft.living.pufferfish

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityData
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.water.PufferFishMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("puff_state_data", "State of the Puf entity", Colors.BLUE, "mdi:state-machine")
@Tags("puff_state_data", "puffer_fish_data")
class PuffStateData(
    override val id: String = "",
    override val name: String = "",
    val state: PufferFishMeta.State = PufferFishMeta.State.UNPUFFED,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<PuffStateProperty> {
    override fun type(): KClass<PuffStateProperty> = PuffStateProperty::class

    override fun build(player: Player): PuffStateProperty = PuffStateProperty(state)
}

data class PuffStateProperty(val state: PufferFishMeta.State) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<PuffStateProperty>(
        PuffStateProperty::class,
        PuffStateProperty(PufferFishMeta.State.UNPUFFED)
    )
}

fun applyPuffStateData(entity: WrapperEntity, property: PuffStateProperty) {
    entity.metas {
        meta<PufferFishMeta> { state = property.state }
        error("Could not apply PufStateData to ${entity.entityType} entity.")
    }
}