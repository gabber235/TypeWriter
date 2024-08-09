package me.gabber235.typewriter.entries.entity.custom

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.minecraft.living.toProperty
import me.gabber235.typewriter.entries.entity.minecraft.PlayerEntity
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.utils.Sound
import org.bukkit.entity.Player
import java.util.*

@Entry("self_npc_definition", "The definition of the self NPC", Colors.ORANGE, "mdi:account")
/**
 * The `Self NPC Definition` entry that defines a player entity with the skin of the viewer.
 *
 * ## How could this be used?
 * Showing the player themselves during a cinematic.
 */
class SelfNpcDefinition(
    override val id: String = "",
    override val name: String = "",
    @Help("Overrides the display name of the speaker")
    val overrideName: Optional<String> = Optional.empty(),
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "player_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override val displayName: String
        get() = overrideName.orElseGet { "%player_name%" }

    override fun create(player: Player): FakeEntity = SelfNpc(player)
}

class SelfNpc(
    player: Player,
) : FakeEntity(player) {
    private val playerEntity = PlayerEntity(player, player.name)

    override val state: EntityState
        get() = playerEntity.state

    init {
        setup()
    }

    private fun setup() {
        consumeProperties(player.skin, player.equipment.toProperty())
    }

    override val entityId: Int
        get() = playerEntity.entityId

    override fun applyProperties(properties: List<EntityProperty>) {
        playerEntity.consumeProperties(properties)
    }

    override fun tick() {
        playerEntity.tick()
    }

    override fun spawn(location: LocationProperty) {
        // When in a cinematic, the equipment will be reset to empty. We want to keep the player's equipment.
        super.spawn(location)
        setup()
        playerEntity.spawn(location)
    }

    override fun addPassenger(entity: FakeEntity) {
        playerEntity.addPassenger(entity)
    }

    override fun removePassenger(entity: FakeEntity) {
        playerEntity.removePassenger(entity)
    }

    override fun contains(entityId: Int): Boolean = playerEntity.contains(entityId)

    override fun dispose() {
        super.dispose()
        playerEntity.dispose()
    }
}