package me.gabber235.typewriter.extensions.protocollib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers
import me.gabber235.typewriter.capture.capturers.ArmSwing
import org.bukkit.entity.Player

fun Player.spectateEntity(entity: ClientEntity) {
    entity.spectateThis(this)
}

fun Player.stopSpectatingEntity() {
    val packet = PacketContainer(PacketType.Play.Server.CAMERA)
    packet.integers.write(0, entityId)
    ProtocolLibrary.getProtocolManager().sendServerPacket(this, packet, false)
}

fun Player.mount(entity: ClientEntity) {
    entity.mount(this)
}

fun Player.unmount(entity: ClientEntity) {
    entity.unmount(this)
}

fun Player.swingArm(entityId: Int, armSwing: ArmSwing) {
    if (armSwing.swingLeft) {
        swingArm(entityId, EnumWrappers.Hand.OFF_HAND)
    }
    if (armSwing.swingRight) {
        swingArm(entityId, EnumWrappers.Hand.MAIN_HAND)
    }
}

fun Player.swingArm(entityId: Int, hand: EnumWrappers.Hand) {
    val packet = PacketContainer(PacketType.Play.Server.ANIMATION)
    packet.integers.write(0, entityId)
    packet.integers.write(1, if (hand == EnumWrappers.Hand.MAIN_HAND) 0 else 3)
    ProtocolLibrary.getProtocolManager().sendServerPacket(this, packet, false)
}