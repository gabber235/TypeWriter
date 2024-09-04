package com.typewritermc.engine.paper.entry.entity

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import lirand.api.extensions.server.server
import com.typewritermc.engine.paper.entry.AudienceManager
import com.typewritermc.engine.paper.events.AsyncEntityDefinitionInteract
import com.typewritermc.engine.paper.events.AsyncFakeEntityInteract
import com.typewritermc.engine.paper.plugin
import me.tofaa.entitylib.APIConfig
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EntityHandler : PacketListenerAbstract(), KoinComponent {
    private val audienceManager: AudienceManager by inject()
    fun initialize() {
        val platform = SpigotEntityLibPlatform(plugin)
        val settings = APIConfig(PacketEvents.getAPI())
            .usePlatformLogger()

        EntityLib.init(platform, settings)

        PacketEvents.getAPI().eventManager.registerListener(this)
    }

    override fun onPacketReceive(event: PacketReceiveEvent?) {
        if (event == null) return
        if (event.packetType != Play.Client.INTERACT_ENTITY) return
        val packet = WrapperPlayClientInteractEntity(event)

        val entityId = packet.entityId
        val player = event.getPlayer<Player>() ?: server.getPlayer(event.user.uuid) ?: return

        AsyncFakeEntityInteract(player, entityId, packet.hand, packet.action).callEvent()

        val display = audienceManager
            .findDisplays(ActivityEntityDisplay::class)
            .firstOrNull { it.playerSeesEntity(event.user.uuid, entityId) } ?: return

        val definition = display.definition ?: return
        AsyncEntityDefinitionInteract(player, entityId, definition, packet.hand, packet.action).callEvent()
    }

    fun shutdown() {
        PacketEvents.getAPI().eventManager.unregisterListener(this)
    }
}