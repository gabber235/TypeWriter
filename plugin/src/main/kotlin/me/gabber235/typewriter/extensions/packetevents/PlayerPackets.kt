package me.gabber235.typewriter.extensions.packetevents

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.util.Vector3i
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_OFF_HAND
import me.gabber235.typewriter.capture.capturers.ArmSwing
import me.tofaa.entitylib.meta.EntityMeta
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.Location
import org.bukkit.entity.Player

infix fun <T : PacketWrapper<T>> Player.sendPacket(packet: PacketWrapper<T>) {
    PacketEvents.getAPI().playerManager.sendPacket(this, packet)
}

infix fun <T : PacketWrapper<T>> T.sendPacketTo(player: Player) {
    PacketEvents.getAPI().playerManager.sendPacket(player, this)
}

fun Player.spectateEntity(entity: WrapperEntity) = setCamera(entityId = entity.entityId)

fun Player.stopSpectatingEntity() = setCamera(entityId = entityId)

private fun Player.setCamera(entityId: Int) = WrapperPlayServerCamera(entityId).sendPacketTo(this)

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

fun Location.toVector3i() =
    Vector3i(blockX, blockY, blockZ)