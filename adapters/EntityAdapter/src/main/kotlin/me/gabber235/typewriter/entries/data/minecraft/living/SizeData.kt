package me.gabber235.typewriter.entries.data.minecraft.living

import me.gabber235.typewriter.adapters.modifiers.Help

import me.gabber235.typewriter.adapters.*
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.mobs.cuboid.MagmaCubeMeta
import me.tofaa.entitylib.meta.mobs.cuboid.SlimeMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("size_data", "Size of the entity", Colors.RED, "mdi:resize")
@Tags("size_data", "slime_data", "magma_cube_data")
class SizeData(
    override val id: String = "",
    override val name: String = "",
    @Help("The size of the entity.")
    val size: Int = 1,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : EntityData<SizeProperty> {
    override fun type(): KClass<SizeProperty> = SizeProperty::class

    override fun build(player: Player): SizeProperty = SizeProperty(size)
}

data class SizeProperty(val size: Int) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<SizeProperty>(SizeProperty::class, SizeProperty(1))
}

fun applySizeData(entity: WrapperEntity, property: SizeProperty) {
    entity.metas {
         meta<SlimeMeta> { size = property.size }
         meta<MagmaCubeMeta> { size = property.size }
        error("Could not apply SizeData to ${entity.entityType} entity.")
    }
}