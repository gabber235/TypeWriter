package me.gabber235.typewriter.entries.data.minecraft.display.item

import io.github.retrooper.packetevents.util.SpigotConversionUtil
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entries.data.minecraft.display.DisplayEntityData
import me.gabber235.typewriter.entry.entity.SinglePropertyCollectorSupplier
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.metas
import me.gabber235.typewriter.utils.Item
import me.tofaa.entitylib.meta.display.ItemDisplayMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KClass

@Entry("item_data", "Item for a display", Colors.RED, "mdi:tools")
@Tags("item_data")
class ItemData(
    override val id: String = "",
    override val name: String = "",
    @Help("Item ID")
    val item: Item  = Item.Empty,
    override val priorityOverride: Optional<Int> = Optional.empty(),
) : DisplayEntityData<ItemProperty> {
    override val type: KClass<ItemProperty> = ItemProperty::class

    override fun build(player: Player): ItemProperty = ItemProperty(item)
}

data class ItemProperty(val item: Item) : EntityProperty {
    companion object : SinglePropertyCollectorSupplier<ItemProperty>(ItemProperty::class)
}

fun applyItemData(entity: WrapperEntity, property: ItemProperty, player: Player) {
    entity.metas {
        meta<ItemDisplayMeta> { item = SpigotConversionUtil.fromBukkitItemStack(property.item.build(player)) }
        error("Could not apply ItemData to ${entity.entityType} entity.")
    }
}