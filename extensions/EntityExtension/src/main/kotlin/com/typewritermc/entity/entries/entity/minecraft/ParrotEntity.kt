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
import com.typewritermc.entity.entries.data.minecraft.DyeColorProperty
import com.typewritermc.entity.entries.data.minecraft.applyDyeColorData
import com.typewritermc.entity.entries.data.minecraft.applyGenericEntityData
import com.typewritermc.entity.entries.data.minecraft.living.applyLivingEntityData
import com.typewritermc.entity.entries.data.minecraft.living.parrot.ParrotColorProperty
import com.typewritermc.entity.entries.data.minecraft.living.parrot.applyParrotColorData
import com.typewritermc.entity.entries.entity.WrapperFakeEntity
import org.bukkit.entity.Player

@Entry("parrot_definition", "A parrot entity", Colors.ORANGE, "ph:bird-fill")
@Tags("parrot_definition")
/**
 * The `ParrotDefinition` class is an entry that represents a parrot entity.
 *
 * ## How could this be used?
 * This could be used to create a parrot entity.
 */
class ParrotDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: Var<String> = ConstVar(""),
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "parrot_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = ParrotEntity(player)
}

@Entry("parrot_instance", "An instance of a parrot entity", Colors.YELLOW, "ph:bird-fill")
/**
 * The `Parrot Instance` class is an entry that represents an instance of a parrot entity.
 *
 * ## How could this be used?
 *
 * This could be used to create a parrot entity.
 */
class ParrotInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<ParrotDefinition> = emptyRef(),
    override val spawnLocation: Position = Position.ORIGIN,
    @OnlyTags("generic_entity_data", "living_entity_data", "parrot_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

private class ParrotEntity(player: Player) : WrapperFakeEntity(EntityTypes.PARROT, player) {
    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is ParrotColorProperty -> applyParrotColorData(entity, property)
        }
        if (applyGenericEntityData(entity, property)) return
        if (applyLivingEntityData(entity, property)) return
    }

}