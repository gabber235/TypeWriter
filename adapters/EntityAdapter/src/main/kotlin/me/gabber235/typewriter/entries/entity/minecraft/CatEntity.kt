package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.minecraft.applyGenericEntityData
import me.gabber235.typewriter.entries.data.minecraft.living.CollarColorProperty
import me.gabber235.typewriter.entries.data.minecraft.living.applyCollarColorData
import me.gabber235.typewriter.entries.data.minecraft.living.applyLivingEntityData
import me.gabber235.typewriter.entries.data.minecraft.living.cat.CatVariantProperty
import me.gabber235.typewriter.entries.data.minecraft.living.cat.applyCatVariantData
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

@Entry("cat_definition", "A cat entity", Colors.ORANGE, "ph:cat-fill")
@Tags("cat_definition")
/**
 * The `CatDefinition` class is an entry that represents a cat entity.
 *
 * ## How could this be used?
 * This could be used to create a cat entity.
 */
class CatDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "cat_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = CatEntity(player)
}

@Entry("cat_instance", "An instance of a cat entity", Colors.YELLOW, "ph:cat-fill")
/**
 * The `Cat Instance` class is an entry that represents an instance of a cat entity.
 *
 * ## How could this be used?
 *
 * This could be used to create a cat entity.
 */
class CatInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<CatDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "living_entity_data", "cat_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

private class CatEntity(player: Player) : WrapperFakeEntity(EntityTypes.CAT,player) {
    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is CatVariantProperty -> applyCatVariantData(entity, property)
            is CollarColorProperty -> applyCollarColorData(entity, property)
        }
        if (applyGenericEntityData(entity, property)) return
        if (applyLivingEntityData(entity, property)) return
    }

}