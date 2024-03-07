package me.gabber235.typewriter.entry.entity

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity
import lirand.api.extensions.server.server
import me.gabber235.typewriter.entry.AudienceManager
import me.gabber235.typewriter.events.AsyncFakeEntityInteract
import me.gabber235.typewriter.plugin
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
            .debugMode()
            .usePlatformLogger()

        EntityLib.init(platform, settings)

        PacketEvents.getAPI().eventManager.registerListener(this)
    }

    override fun onPacketReceive(event: PacketReceiveEvent?) {
        if(event == null) return
        if (event.packetType != Play.Client.INTERACT_ENTITY) return
        val packet = WrapperPlayClientInteractEntity(event)
        val entityId = packet.entityId

        val display = audienceManager
            .findDisplays(ActivityEntityDisplay::class)
            .firstOrNull { it.playerHasEntity(event.user.uuid, entityId) } ?: return

        val definition = display.definition ?: return
        val player = event.player as? Player ?: server.getPlayer(event.user.uuid) ?: return
        AsyncFakeEntityInteract(player, entityId, definition).callEvent()
    }

    fun shutdown() {
        PacketEvents.getAPI().eventManager.unregisterListener(this)
    }
}