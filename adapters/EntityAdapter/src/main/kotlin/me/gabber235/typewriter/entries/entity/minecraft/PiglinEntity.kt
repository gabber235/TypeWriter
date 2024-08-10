package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.minecraft.applyGenericEntityData
import me.gabber235.typewriter.entries.data.minecraft.living.*
import me.gabber235.typewriter.entries.data.minecraft.living.piglin.DancingProperty
import me.gabber235.typewriter.entries.data.minecraft.living.piglin.applyDancingData
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

@Entry("piglin_definition", "A piglin entity", Colors.ORANGE, "fluent-emoji-high-contrast:pig-nose")
@Tags("piglin_definition")
/**
 * The `PiglinDefinition` class is an entry that shows up as a piglin in-game.
 *
 * ## How could this be used?
 * This could be used to create a piglin entity.
 */
class PiglinDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags(
        "generic_entity_data",
        "living_entity_data",
        "mob_data",
        "ageable_data",
        "piglin_data",
        "piglin_dancing_data"
    )
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = PiglinEntity(player)
}

@Entry("piglin_instance", "An instance of a piglin entity", Colors.YELLOW, "fluent-emoji-high-contrast:pig-nose")
class PiglinInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<PiglinDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags(
        "generic_entity_data",
        "living_entity_data",
        "mob_data",
        "ageable_data",
        "piglin_data",
        "piglin_dancing_data"
    )
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

private class PiglinEntity(player: Player) : WrapperFakeEntity(
    EntityTypes.PIGLIN,
    player,
) {
    init {
        consumeProperties(TremblingProperty(false))
    }

    override fun applyProperty(property: EntityProperty) {
        when (property) {
            is DancingProperty -> applyDancingData(entity, property)
            is AgableProperty -> applyAgeableData(entity, property)
            is TremblingProperty -> applyTremblingData(entity, property)
            else -> {}
        }
        if (applyGenericEntityData(entity, property)) return
        if (applyLivingEntityData(entity, property)) return
    }
}

