package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.player.UserProfile
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.FakeEntity
import me.gabber235.typewriter.entry.entity.LocationProperty
import me.gabber235.typewriter.entry.entity.SimpleEntityDefinition
import me.gabber235.typewriter.entry.entity.SimpleEntityInstance
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.toPacketLocation
import me.gabber235.typewriter.utils.Sound
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.spigot.SpigotEntityLibAPI
import me.tofaa.entitylib.wrapper.WrapperPlayer
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

@Entry("player_definition", "A player entity", Colors.ORANGE, "material-symbols:account-box")
@Tags("player_definition")
/**
 * The `PlayerDefinition` class is an entry that represents a player entity.
 * It is a bare bone's version of a `NPC` entity.
 *
 * ## How could this be used?
 * This could be used to create a player entity.
 */
class PlayerDefinition(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
    @OnlyTags("generic_entity_data", "living_entity_data", "player_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
) : SimpleEntityDefinition {
    override fun create(player: Player): FakeEntity = PlayerEntity(player)
}

@Entry("player_instance", "An instance of a player entity", Colors.YELLOW, "material-symbols:account-box")
/**
 * The `PlayerInstance` class is an entry that represents an instance of a player entity.
 */
class PlayerInstance(
    override val id: String = "",
    override val name: String = "",
    override val definition: Ref<PlayerDefinition> = emptyRef(),
    @OnlyTags("generic_entity_data", "living_entity_data", "player_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activities: List<Ref<out EntityActivityEntry>> = emptyList(),
) : SimpleEntityInstance

private class PlayerEntity(player: Player) : FakeEntity(player) {
    private var entity: WrapperPlayer? = null
    override val entityId: Int
        get() = entity?.entityId ?: -1

    override fun applyProperties(properties: List<EntityProperty>) {
        if (entity == null) return
        properties.forEach { property ->
            when (property) {
                is LocationProperty -> entity?.teleport(property.location.toPacketLocation())
                else -> {}
            }
        }
    }

    override fun spawn(location: Location) {
        val id = UUID.randomUUID()
        entity = EntityLib.getApi<SpigotEntityLibAPI>()
            .spawnPlayer(UserProfile(id, id.toString()), location.toPacketLocation())

        applyProperties(properties.values.toList())
    }

    override fun addPassenger(entity: FakeEntity) {
        val player = this.entity ?: return
        player.addPassenger(entity.entityId)
    }

    override fun dispose() {
        entity?.despawn()
        entity?.remove()
    }
}