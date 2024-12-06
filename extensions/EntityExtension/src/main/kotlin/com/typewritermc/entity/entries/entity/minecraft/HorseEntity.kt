package com.typewritermc.entity.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.OnlyTags
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.entity.FakeEntity
import com.typewritermc.engine.paper.entry.entity.SimpleEntityDefinition
import com.typewritermc.engine.paper.entry.entity.SimpleEntityInstance
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.utils.Sound
import com.typewritermc.entity.entries.data.minecraft.applyGenericEntityData
import com.typewritermc.entity.entries.data.minecraft.living.AgeableProperty
import com.typewritermc.entity.entries.data.minecraft.living.SaddledProperty
import com.typewritermc.entity.entries.data.minecraft.living.applyAgeableData
import com.typewritermc.entity.entries.data.minecraft.living.applyLivingEntityData
import com.typewritermc.entity.entries.data.minecraft.living.applySaddledData
import com.typewritermc.entity.entries.data.minecraft.living.horse.*
import com.typewritermc.entity.entries.entity.WrapperFakeEntity
import org.bukkit.entity.Player

@Entry("horse_definition", "A horse entity", Colors.ORANGE, "mdi:horse")
@Tags("horse_definition")
/**
 * The `HorseDefinition` class is an entry that represents a horse entity.
 *
 * ## How could this be used?
 * This could be used to create a horse entity.
 */
class HorseDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: Var<String> = ConstVar(""),
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "horse_variant_data", "horse_data", "eating_data", "chested_horse_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = HorseEntity(player)
}

@Entry("horse_instance", "An instance of a horse entity", Colors.YELLOW, "mdi:horse")
/**
 * The `HorseInstance` class is an entry that represents an instance of a horse entity.
 *
 * ## How could this be used?
 *
 * This could be used to create a horse entity.
 */
class HorseInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<HorseDefinition> = emptyRef(),
    override val spawnLocation: Position = Position.ORIGIN,
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "horse_variant_data", "horse_data", "eating_data", "chested_horse_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

private class HorseEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.HORSE,
    player,
) {
    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is AgeableProperty -> applyAgeableData(entity, property)
            is HorseVariantProperty -> applyHorseVariantData(entity, property)
            is ChestedHorseChestProperty -> applyChestedHorseChestData(entity, property)
            is SaddledProperty -> applySaddledData(entity, property)
            else -> {}
        }
        if (applyGenericEntityData(entity, property)) return
        if (applyLivingEntityData(entity, property)) return
    }
}