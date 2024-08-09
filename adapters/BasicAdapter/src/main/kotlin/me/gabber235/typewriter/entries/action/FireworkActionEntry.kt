package me.gabber235.typewriter.entries.action

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus
import lirand.api.extensions.inventory.meta
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.extensions.packetevents.sendPacketTo
import me.gabber235.typewriter.extensions.packetevents.toPacketItem
import me.gabber235.typewriter.extensions.packetevents.toPacketLocation
import me.gabber235.typewriter.utils.Color
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.meta.Metadata
import me.tofaa.entitylib.meta.other.FireworkRocketMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta
import java.util.*

private const val FIREWORK_EXPLOSION_STATUS = 17

@Entry("firework", "Spawns a firework", Colors.RED, "streamline:fireworks-rocket-solid")
/**
 * The `Firework Action Entry` is an action that spawns a firework.
 *
 * ## How could this be used?
 * This could be used to create a firework that displays a specific effect.
 */
class FireworkActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    @Help("The location to spawn the firework.")
    val location: Location = Location(null, 0.0, 0.0, 0.0),
    @Help("The effects to display on the firework.")
    val effects: List<FireworkEffectConfig> = emptyList(),
    @Help("The power of the firework.")
    val power: Int = 0,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val item = ItemStack(Material.FIREWORK_ROCKET).meta<FireworkMeta> {
            this@FireworkActionEntry.effects.forEach { effect ->
                addEffect(effect.toBukkitEffect())
            }
            power = this@FireworkActionEntry.power
        }

        val uuid = UUID.randomUUID()
        val entityId = EntityLib.getPlatform().entityIdProvider.provide(uuid, EntityTypes.FIREWORK_ROCKET)
        val meta = FireworkRocketMeta(entityId, Metadata(entityId)).apply {
            fireworkItem = item.toPacketItem()
        }
        val entity = WrapperEntity(entityId, uuid, EntityTypes.FIREWORK_ROCKET, meta)
        entity.addViewer(player.uniqueId)
        entity.spawn(location.toPacketLocation())
        WrapperPlayServerEntityStatus(entityId, FIREWORK_EXPLOSION_STATUS) sendPacketTo player
        entity.despawn()
    }
}

class FireworkEffectConfig(
    val type: FireworkEffect.Type = FireworkEffect.Type.BALL,
    val flicker: Boolean = false,
    val trail: Boolean = false,
    val colors: List<Color> = emptyList(),
    val fadeColors: List<Color> = emptyList(),
) {
    fun toBukkitEffect(): FireworkEffect {
        return FireworkEffect.builder()
            .with(type)
            .trail(trail)
            .flicker(flicker)
            .withColor(colors.map { it.toBukkitColor() })
            .withFade(fadeColors.map { it.toBukkitColor() })
            .build()
    }
}