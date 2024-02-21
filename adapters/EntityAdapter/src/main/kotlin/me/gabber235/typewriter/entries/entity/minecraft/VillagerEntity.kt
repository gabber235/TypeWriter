package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.AgableProperty
import me.gabber235.typewriter.entries.data.VillagerProperty
import me.gabber235.typewriter.entries.data.applyAgeableData
import me.gabber235.typewriter.entries.data.applyVillagerData
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


@Entry("villager_definition", "A villager entity", Colors.ORANGE, "material-symbols:diamond")
@Tags("villager_definition")
class VillagerDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "villager_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = VillagerEntity(player)
}

@Entry("villager_instance", "An instance of a villager entity", Colors.YELLOW, "material-symbols:diamond")
class VillagerInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<VillagerDefinition> = emptyRef(),
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "villager_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activities: List<Ref<out EntityActivityEntry>> = emptyList(),
) : SimpleEntityInstance

private class VillagerEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.VILLAGER,
    player,
) {
    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is AgableProperty -> applyAgeableData(entity, property)
            is VillagerProperty -> applyVillagerData(entity, property)
            else -> {}
        }
    }
}