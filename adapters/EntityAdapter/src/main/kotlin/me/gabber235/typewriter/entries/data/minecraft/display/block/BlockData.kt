package me.gabber235.typewriter.entries.data.minecraft.display.block

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entries.data.minecraft.display.DisplayEntityData
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.tofaa.entitylib.meta.display.BlockDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("block_data", "Block for a display", Colors.RED, "mage:box-3d-fill")

@Tags("block_data")

class BlockData(
    override val id: String = "",
    override val name: String = "",
    @Help("Block ID")
    val blockId: Int = 0,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<BlockProperty> {
    override val type: KClass<BlockProperty> = BlockProperty::class

    override fun build(player: Player): BlockProperty = BlockProperty(blockId)
}

data class BlockProperty(val blockId: Int) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<BlockProperty>(BlockProperty::class)
}

fun applyBlockData(entity: WrapperEntity, property: BlockProperty) {
    entity.metas {
        meta<BlockDisplayMeta> { blockId = property.blockId }
        error("Could not apply BlockData to ${entity.entityType} entity.")
    }
}