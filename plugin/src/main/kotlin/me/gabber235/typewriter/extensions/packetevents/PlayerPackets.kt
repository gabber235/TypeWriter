package me.gabber235.typewriter.extensions.packetevents

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.wrapper.PacketWrapper
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_MAIN_ARM
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityAnimation.EntityAnimationType.SWING_OFF_HAND
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRotation
import me.gabber235.typewriter.capture.capturers.ArmSwing
import me.tofaa.entitylib.entity.WrapperEntity
import me.tofaa.entitylib.meta.EntityMeta
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

inline fun <reified E : EntityMeta> WrapperEntity.meta(editor: E.() -> Unit): WrapperEntity {
    val meta = meta
    if (meta is E) {
        editor(meta)
    }
    return this
}

fun Location.toPacketLocation() = com.github.retrooper.packetevents.protocol.world.Location(x, y, z, yaw, pitch)