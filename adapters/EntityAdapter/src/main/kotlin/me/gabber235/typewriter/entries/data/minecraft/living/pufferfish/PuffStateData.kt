package me.gabber235.typewriter.entries.data.minecraft.living.pufferfish

import me.gabber235.typewriter.adapters.modifiers.Help

import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.water.PufferFishMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("puff_state_data", "State of the Puf entity", Colors.BLUE, "mdi:state")
@Tags("puff_state_data", "puffer_fish_data")
class PuffStateData(
    override val id: String = "",
    override val name: String = "",
    @Help("The state of the Puf entity.")
    val state: PufferFishMeta.State = PufferFishMeta.State.UNPUFFED,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<PuffStateProperty> {
    override fun type(): KClass<PuffStateProperty> = PuffStateProperty::class

    override fun build(player: Player): PuffStateProperty = PuffStateProperty(state)
}

data class PuffStateProperty(val state: PufferFishMeta.State) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<PuffStateProperty>(PuffStateProperty::class, PuffStateProperty(PufferFishMeta.State.UNPUFFED))
}

fun applyPuffStateData(entity: WrapperEntity, property: PuffStateProperty) {
     entity.metas {
         meta<PufferFishMeta> { state = property.state }
         error("Could not apply PufStateData to ${entity.entityType} entity.")
     }
}