package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.minecraft.applyGenericEntityData
import me.gabber235.typewriter.entries.data.minecraft.living.*
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

@Entry("magma_cube_definition", "A magma cube entity", Colors.ORANGE, "ph:magma-cube-fill")
@Tags("magma_cube_definition")
/**
 * The `MagmaCubeDefinition` class is an entry that represents a magma cube entity.
 *
 * ## How could this be used?
 * This could be used to create a magma cube entity.
 */
class MagmaCubeDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "slime_data", "magma_cube_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = MagmaCubeEntity(player)
}

@Entry("magma_cube_instance", "An instance of a magma cube entity", Colors.YELLOW, "ph:magma-cube-fill")
/**
 * The `MagmaCubeInstance` class is an entry that represents an instance of a magma cube entity.
 *
 * ## How could this be used?
 *
 * This could be used to create a magma cube entity.
 */
class MagmaCubeInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<MagmaCubeDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "slime_data", "magma_cube_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

private class MagmaCubeEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.MAGMA_CUBE,
    player,
) {
    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is SizeProperty -> applySizeData(entity, property)
            else -> {}
        }
        if (applyGenericEntityData(entity, property)) return
        if (applyLivingEntityData(entity, property)) return
    }
}