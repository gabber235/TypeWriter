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

@Entry("llaama_definition", "A llama entity", Colors.ORANGE, "ph:llama-fill")
@Tags("llama_definition")
/**
 * The `LlamaDefinition` class is an entry that represents a llama entity.
 *
 * ## How could this be used?
 * This could be used to create a llama entity.
 */
class LlamaDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "llama_data", "chested_horse_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = LlamaEntity(player)
}

@Entry("llama_instance", "An instance of a llama entity", Colors.YELLOW, "ph:llama-fill")
/**
 * The `LlamaInstance` class is an entry that represents an instance of a llama entity.
 *
 * ## How could this be used?
 *
 * This could be used to create a llama entity.
 */
class LlamaInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<LlamaDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "llama_data", "chested_horse_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

private class LlamaEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.LLAMA,
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