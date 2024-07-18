package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.minecraft.applyGenericEntityData
import me.gabber235.typewriter.entries.data.minecraft.display.applyDisplayEntityData
import me.gabber235.typewriter.entries.data.minecraft.display.item.DisplayTypeProperty
import me.gabber235.typewriter.entries.data.minecraft.display.item.ItemProperty
import me.gabber235.typewriter.entries.data.minecraft.display.item.applyDisplayTypeData
import me.gabber235.typewriter.entries.data.minecraft.display.item.applyItemData
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.SimpleEntityDefinition
import me.gabber235.typewriter.entry.entity.SimpleEntityInstance
import me.gabber235.typewriter.entries.entity.WrapperFakeEntity
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.entry.entries.SharedEntityActivityEntry
import me.gabber235.typewriter.utils.Sound
import org.bukkit.Location
import org.bukkit.entity.Player

@Entry("item_display_definition", "An item display entity", Colors.ORANGE, "icon-park-solid:holy-sword")
@Tags("item_display_definition")
/**
 * The `ItemDisplayDefinition` class is an entry that represents an item display entity.
 *
 * ## How could this be used?
 * This could be used to create an entity that displays an item.
 */
class ItemDisplayDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "display_data", "item_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = ItemDisplayEntity(player)
}

@Entry(
    "item_display_instance",
    "An instance of an item display entity",
    Colors.YELLOW,
    "material-symbols:aspect_ratio"
)

class ItemDisplayInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<ItemDisplayDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "display_data", "item_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

open class ItemDisplayEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.ITEM_DISPLAY,
    player,
) {
    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is ItemProperty -> applyItemData(entity, property, player)
            is DisplayTypeProperty -> applyDisplayTypeData(entity, property)
        }
        if (applyGenericEntityData(entity, property)) return
        if (applyDisplayEntityData(entity, property)) return
    }
}