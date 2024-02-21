package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.AgableProperty
import me.gabber235.typewriter.entries.data.applyAgeableData
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.SimpleEntityDefinition
import me.gabber235.typewriter.entry.entity.SimpleEntityInstance
import me.gabber235.typewriter.entry.entity.WrapperFakeEntity
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.utils.Sound
import org.bukkit.entity.Player

@Entry("cow_definition", "A cow entity", Colors.ORANGE, "fa6-solid:cow")
@Tags("cow_definition")
class CowDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "cow_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = CowEntity(player)
}

@Entry("cow_instance", "An instance of a cow entity", Colors.YELLOW, "fa6-solid:cow")
class CowInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<CowDefinition> = emptyRef(),
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "cow_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activities: List<Ref<out EntityActivityEntry>> = emptyList(),
) : SimpleEntityInstance

private class CowEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.COW,
    player,
) {
    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is AgableProperty -> applyAgeableData(entity, property)
            else -> {}
        }
    }
}