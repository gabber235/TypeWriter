package com.typewritermc.engine.paper.extensions.packetevents

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.util.Vector3d
import com.github.retrooper.packetevents.util.Vector3i
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_OFF_HAND
import io.github.retrooper.packetevents.util.SpigotConversionUtil
import me.tofaa.entitylib.meta.EntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

infix fun <T : PacketWrapper<T>> Player.sendPacket(packet: PacketWrapper<T>) {
    PacketEvents.getAPI().playerManager.sendPacket(this, packet)
}

infix fun <T : PacketWrapper<T>> T.sendPacketTo(player: Player) {
    PacketEvents.getAPI().playerManager.sendPacket(player, this)
}

fun Player.spectateEntity(entity: WrapperEntity) = setCamera(entityId = entity.entityId)

fun Player.stopSpectatingEntity() = setCamera(entityId = entityId)

private fun Player.setCamera(entityId: Int) = WrapperPlayServerCamera(entityId).sendPacketTo(this)

enum class ArmSwing {
    LEFT, RIGHT, BOTH;

    val swingLeft: Boolean get() = this == LEFT || this == BOTH
    val swingRight: Boolean get() = this == RIGHT || this == BOTH
}

fun Player.swingArm(entityId: Int, armSwing: ArmSwing) {
    if (armSwing.swingLeft) {
        WrapperPlayServerEntityAnimation(entityId, SWING_OFF_HAND).sendPacketTo(this)
    }
    if (armSwing.swingRight) {
        WrapperPlayServerEntityAnimation(entityId, SWING_MAIN_ARM).sendPacketTo(this)
    }
}

inline fun <reified E : EntityMeta> WrapperEntity.meta(
    preventNotification: Boolean = false,
    editor: E.() -> Unit
): WrapperEntity {
    val meta = entityMeta
    if (meta is E) {
        if (!preventNotification) meta.setNotifyAboutChanges(false)
        editor(meta)
        if (!preventNotification) meta.setNotifyAboutChanges(true)
    }
    return this
}

class Metas(val meta: EntityMeta) {
    var hasBeenHandled = false
    internal var error: String? = null
    inline fun <reified E : EntityMeta> meta(editor: E.() -> Unit) {
        if (meta is E) {
            editor(meta)
            hasBeenHandled = true
        }
    }

    fun error(error: String) {
        this.error = error
    }
}

fun WrapperEntity.metas(editor: Metas.() -> Unit) {
    val meta = entityMeta
    val metas = Metas(meta).apply(editor)
    if (!metas.hasBeenHandled) {
        throw IllegalStateException(metas.error ?: "No meta was handled")
    }
}

fun Location.toPacketLocation() = com.github.retrooper.packetevents.protocol.world.Location(x, y, z, yaw, pitch)

fun ItemStack.toPacketItem() = SpigotConversionUtil.fromBukkitItemStack(this)

fun Location.toVector3i() =
    Vector3i(blockX, blockY, blockZ)

fun Location.toVector3d() =
    Vector3d(x, y, z)
