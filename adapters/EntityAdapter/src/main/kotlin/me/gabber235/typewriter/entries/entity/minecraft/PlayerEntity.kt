package me.gabber235.typewriter.entries.entity.minecraft

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.player.TextureProperty
import com.github.retrooper.packetevents.protocol.player.UserProfile
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.OnlyTags
import me.gabber235.typewriter.entries.data.minecraft.applyGenericEntityData
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entity.*
import me.gabber235.typewriter.entry.entries.EntityActivityEntry
import me.gabber235.typewriter.entry.entries.EntityData
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.extensions.packetevents.meta
import me.gabber235.typewriter.extensions.packetevents.sendPacketTo
import me.gabber235.typewriter.utils.Sound
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.meta.types.PlayerMeta
import me.tofaa.entitylib.spigot.SpigotEntityLibAPI
import me.tofaa.entitylib.wrapper.WrapperPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
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
    override val spawnLocation: Location = Location(null, 0.0, 0.0, 0.0),
    @OnlyTags("generic_entity_data", "living_entity_data", "player_data")
    override val data: List<Ref<EntityData<*>>> = emptyList(),
    override val activities: List<Ref<out EntityActivityEntry>> = emptyList(),
) : SimpleEntityInstance

class PlayerEntity(
    player: Player,
) : FakeEntity(player) {
    private var entity: WrapperPlayer
    override val entityId: Int
        get() = entity.entityId

    init {
        val uuid = UUID.randomUUID()
        var entityId: Int
        do {
            entityId = EntityLib.getPlatform().entityIdProvider.provide(uuid, EntityTypes.PLAYER)
        } while (EntityLib.getApi<SpigotEntityLibAPI>().getEntity(entityId) != null)

        entity = EntityLib.getApi<SpigotEntityLibAPI>()
            .createPlayer(UserProfile(uuid, entityId.toString()), entityId)

        entity.isInTablist = false
        entity.meta<PlayerMeta> {
            isCapeEnabled = true
            isHatEnabled = true
            isJacketEnabled = true
            isLeftSleeveEnabled = true
            isRightSleeveEnabled = true
            isLeftLegEnabled = true
            isRightLegEnabled = true
        }
    }


    override fun applyProperties(properties: List<EntityProperty>) {
        properties.forEach { property ->
            when (property) {
                is LocationProperty -> {
                    entity.teleport(property.toPacketLocation())
                    entity.rotateHead(property.yaw, property.pitch)
                }

                is SkinProperty -> entity.textureProperties =
                    listOf(TextureProperty("textures", property.texture, property.signature))

                else -> {
                }
            }
            if (applyGenericEntityData(entity, property)) return
        }
    }

    override fun spawn(location: LocationProperty) {
        entity.spawn(location.toPacketLocation())
        entity.addViewer(player.uniqueId)

        val info = WrapperPlayServerTeams.ScoreBoardTeamInfo(
            Component.empty(),
            null,
            null,
            WrapperPlayServerTeams.NameTagVisibility.NEVER,
            WrapperPlayServerTeams.CollisionRule.NEVER,
            NamedTextColor.WHITE,
            WrapperPlayServerTeams.OptionData.NONE
        )
        WrapperPlayServerTeams(
            "typewriter-$entityId",
            WrapperPlayServerTeams.TeamMode.CREATE,
            info
        ) sendPacketTo player

        WrapperPlayServerTeams(
            "typewriter-$entityId",
            WrapperPlayServerTeams.TeamMode.ADD_ENTITIES,
            Optional.empty(),
            listOf(entityId.toString())
        ) sendPacketTo player
        super.spawn(location)
    }

    override fun addPassenger(entity: FakeEntity) {
        this.entity.addPassenger(entity.entityId)
    }

    override fun removePassenger(entity: FakeEntity) {
        this.entity.removePassenger(entity.entityId)
    }

    override fun contains(entityId: Int): Boolean = this.entityId == entityId

    override fun dispose() {
        WrapperPlayServerTeams(
            "typewriter-$entityId",
            WrapperPlayServerTeams.TeamMode.REMOVE,
            Optional.empty()
        ) sendPacketTo player
        entity.despawn()
        entity.remove()
    }
}