package me.gabber235.typewriter.extensions.protocollib

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
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