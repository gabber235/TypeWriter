package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.minecraft.applyGenericEntityData
import me.gabber235.typewriter.entries.data.minecraft.living.*
import me.gabber235.typewriter.entries.data.minecraft.living.horse.*
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

@Entry("trader_llama_definition", "A trader llama entity", Colors.ORANGE, "ph:llama-fill")
@Tags("trader_llama_definition")
/**
 * The `TraderLlamaDefinition` class is an entry that represents a trader llama entity.
 *
 * ## How could this be used?
 * This could be used to create a trader llama entity.
 */
class TraderLlamaDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "llama_data", "chested_horse_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = TraderLlamaEntity(player)
}

@Entry("trader_llama_instance", "An instance of a trader llama entity", Colors.YELLOW, "ph:llama-fill")
/**
 * The `TraderLlamaInstance` class is an entry that represents an instance of a trader llama entity.
 *
 * ## How could this be used?
 *
 * This could be used to create a trader llama entity.
 */
class TraderLlamaInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<TraderLlamaDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "llama_data", "chested_horse_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

private class TraderLlamaEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.TRADER_LLAMA,
    player,
) {
    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is AgableProperty -> applyAgeableData(entity, property)
            is LlamaVariantProperty -> applyLlamaVariantData(entity, property)
            is LlamaCarpetColorProperty -> applyLlamaCarpetColorData(entity, property)
            is ChestedHorseChestProperty -> applyChestedHorseChestData(entity, property)
            else -> {}
        }
        if (applyGenericEntityData(entity, property)) return
        if (applyLivingEntityData(entity, property)) return
    }
}