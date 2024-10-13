package com.typewritermc.basic.entries.action

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.extensions.packetevents.sendPacketTo
import com.typewritermc.engine.paper.extensions.packetevents.toPacketItem
import com.typewritermc.engine.paper.utils.Color
import com.typewritermc.engine.paper.utils.ThreadType
import com.typewritermc.engine.paper.utils.toPacketLocation
import kotlinx.coroutines.delay
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.meta.Metadata
import me.tofaa.entitylib.meta.other.FireworkRocketMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.FireworkEffect
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.FireworkMeta
import java.time.Duration
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
    val location: Position = Position.ORIGIN,
    val effects: List<FireworkEffectConfig> = emptyList(),
    @Default("0")
    val flightDuration: Duration = Duration.ZERO,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val item = ItemStack(Material.FIREWORK_ROCKET)
            item.editMeta (FireworkMeta::class.java) { meta ->
            this@FireworkActionEntry.effects.forEach { effect ->
                meta.addEffect(effect.toBukkitEffect())
            }
        }

        val uuid = UUID.randomUUID()
        val entityId = EntityLib.getPlatform().entityIdProvider.provide(uuid, EntityTypes.FIREWORK_ROCKET)
        val meta = FireworkRocketMeta(entityId, Metadata(entityId)).apply {
            fireworkItem = item.toPacketItem()
        }
        val entity = WrapperEntity(entityId, uuid, EntityTypes.FIREWORK_ROCKET, meta)
        entity.addViewer(player.uniqueId)
        entity.spawn(location.toPacketLocation())
        if (flightDuration.isZero) {
            WrapperPlayServerEntityStatus(entityId, FIREWORK_EXPLOSION_STATUS) sendPacketTo player
            entity.despawn()
            return
        }
        ThreadType.DISPATCHERS_ASYNC.launch {
            delay(flightDuration.toMillis())
            WrapperPlayServerEntityStatus(entityId, FIREWORK_EXPLOSION_STATUS) sendPacketTo player
            entity.despawn()
        }
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