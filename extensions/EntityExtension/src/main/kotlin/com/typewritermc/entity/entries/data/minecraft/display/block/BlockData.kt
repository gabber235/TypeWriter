package com.typewritermc.entity.entries.data.minecraft.display.block

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.entity.entries.data.minecraft.display.DisplayEntityData
import com.typewritermc.engine.paper.entry.entity.SinglePropertyCollectorSupplier
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.extensions.packetevents.metas
import me.tofaa.entitylib.meta.display.BlockDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("block_data", "Block of a BlockDisplay.", Colors.RED, "mage:box-3d-fill")
@Tags("block_data")
class BlockData(
    override val id: String = "",
    override val name: String = "",
    val blockId: Int = 0,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<BlockProperty> {
    override fun type(): KClass<BlockProperty> = BlockProperty::class

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