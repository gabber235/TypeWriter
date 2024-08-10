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

@Entry("hoglin_definition", "A hoglin entity", Colors.ORANGE, "icon-park-outline:pig")
@Tags("hoglin_definition")
/**
 * The `HoglinDefinition` class is an entry that shows up as a hoglin in-game.
 *
 * ## How could this be used?
 * This could be used to create a hoglin entity.
 */
class HoglinDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "trembling_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = HoglinEntity(player)
}

@Entry("hoglin_instance", "An instance of a hoglin entity", Colors.YELLOW, "icon-park-outline:pig")
class HoglinInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<HoglinDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "living_entity_data", "mob_data", "ageable_data", "trembling_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

private class HoglinEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.HOGLIN,
    player,
) {
    init {
        consumeProperties(TremblingProperty(false))
    }

    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is AgableProperty -> applyAgeableData(entity, property)
            is TremblingProperty -> applyTremblingData(entity, property)
            else -> {}
        }
        if (applyGenericEntityData(entity, property)) return
        if (applyLivingEntityData(entity, property)) return
    }
}