package me.gabber235.typewriter.entries.entity.custom

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.entity.minecraft.PlayerEntity
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.ref
import me.gabber235.typewriter.utils.Sound
import org.bukkit.Location
import org.bukkit.entity.Player


@Entry("npc_definition", "A simplified premade npc", Colors.ORANGE, "material-symbols:account-box")
@Tags("npc_definition")
/**
 * The `NpcDefinition` class is an entry that represents a simplified premade npc.
 *
 * It has an Icon above the head when an `NpcInteractObjective` is active for a player.
 * And when the objective is being tracked, the npc will have a different icon.
 *
 * The npc also has its display name above the head.
 *
 * ## How could this be used?
 * This could be used to create a simple npc has most of the properties already set.
 */
class NpcDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @Help("The skin of the npc.")
    val skin: SkinProperty = SkinProperty(),
    @OnlyTags("generic_entity_data", "living_entity_data", "lines", "player_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {

    override fun create(player: Player): FakeEntity {
        return NpcEntity(player, displayName, skin, ref())
    }
}

@Entry("npc_instance", "An instance of a simplified premade npc", Colors.YELLOW, "material-symbols:account-box")
/**
 * The `NpcInstance` class is an entry that represents an instance of a simplified premade npc.
 */
class NpcInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<NpcDefinition> = emptyRef(),
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "living_entity_data", "lines", "player_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activity: Ref<out SharedEntityActivityEntry> = emptyRef(),
) : SimpleEntityInstance

class NpcEntity(
    player: Player,
    displayName: String,
    private val skin: SkinProperty,
    definition: Ref<out EntityDefinitionEntry>,
) : FakeEntity(player) {
    private val namePlate = NamedEntity(player, displayName, PlayerEntity(player, displayName), definition)

    init {
        consumeProperties(skin)
    }

    override val entityId: Int
        get() = namePlate.entityId

    override val state: EntityState
        get() = namePlate.state

    override fun applyProperties(properties: List<EntityProperty>) {
        if (properties.any { it is SkinProperty }) {
            namePlate.consumeProperties(properties)
            return
        }
        namePlate.consumeProperties(properties + skin)
    }

    override fun tick() {
        namePlate.tick()
    }

    override fun spawn(location: LocationProperty) {
        namePlate.spawn(location)
    }

    override fun addPassenger(entity: FakeEntity) {
        namePlate.addPassenger(entity)
    }

    override fun removePassenger(entity: FakeEntity) {
        namePlate.removePassenger(entity)
    }

    override fun contains(entityId: Int): Boolean {
        return namePlate.contains(entityId)
    }

    override fun dispose() {
        namePlate.dispose()
    }
}