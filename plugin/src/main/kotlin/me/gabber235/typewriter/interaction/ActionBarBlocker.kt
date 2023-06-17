package me.gabber235.typewriter.interaction

import com.comphenix.protocol.PacketType.Play.Server.SET_ACTION_BAR_TEXT
import com.comphenix.protocol.PacketType.Play.Server.SYSTEM_CHAT
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.reflect.StructureModifier
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import lirand.api.extensions.server.server
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.koin.java.KoinJavaComponent.get
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class ActionBarBlockerHandler(plugin: Plugin) :
    PacketAdapter(
        plugin,
        ListenerPriority.NORMAL,
        SYSTEM_CHAT,
        SET_ACTION_BAR_TEXT
    ), Listener {
    fun initialize() {
        ProtocolLibrary.getProtocolManager().addPacketListener(this)
        server.pluginManager.registerSuspendingEvents(this, plugin)
    }

    private val blockers = mutableMapOf<UUID, ActionBarBlocker>()

    override fun onPacketSending(event: PacketEvent) {
        val blocker = blockers[event.player.uniqueId] ?: return

        if (event.packet.type == SYSTEM_CHAT && !event.packet.isActionBar()) return

        val component =
            event.packet.getComponent() ?: return

        if (blocker.isMessageAccepted(component)) return

        event.isCancelled = true
    }

    private fun PacketContainer.getComponent(): Component? {
        return if (type == SYSTEM_CHAT) {
            getChatComponent()
        } else if (type == SET_ACTION_BAR_TEXT) {
            val adventureModifier: StructureModifier<Component>? = getSpecificModifier(Component::class.java)
            adventureModifier?.readSafely(0)
                ?: chatComponentArrays?.readSafely(0)?.firstOrNull()
                    ?.let { GsonComponentSerializer.gson().deserialize(it.json) }
                ?: chatComponents?.readSafely(0)?.json?.let { GsonComponentSerializer.gson().deserialize(it) }
        } else null
    }

    fun acceptMessage(player: Player, message: Component) {
        val blocker = blockers[player.uniqueId] ?: return
        blocker.acceptMessage(message)
    }

    fun enable(player: Player) {
        if (player.uniqueId in blockers) return
        blockers[player.uniqueId] = ActionBarBlocker()
    }

    fun disable(player: Player) {
        blockers.remove(player.uniqueId)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(event: PlayerQuitEvent) {
        blockers.remove(event.player.uniqueId)
    }

    fun shutdown() {
        ProtocolLibrary.getProtocolManager().removePacketListener(this)
    }
}

fun Player.startBlockingActionBar() {
    get<ActionBarBlockerHandler>(ActionBarBlockerHandler::class.java).enable(this)
}

fun Player.stopBlockingActionBar() {
    get<ActionBarBlockerHandler>(ActionBarBlockerHandler::class.java).disable(this)
}

fun Player.acceptActionBarMessage(message: Component) {
    get<ActionBarBlockerHandler>(ActionBarBlockerHandler::class.java).acceptMessage(this, message)
}

/**
 * Keep track of the last 20 accepted messages that are allowed to be sent to the player.
 */
class ActionBarBlocker {
    private val acceptedMessages = ConcurrentLinkedQueue<Component>()

    fun acceptMessage(message: Component) {
        if (isMessageAccepted(message)) return
        acceptedMessages.add(message)
        while (acceptedMessages.size > 20) {
            acceptedMessages.poll()
        }
    }

    fun isMessageAccepted(message: Component): Boolean {
        return acceptedMessages.contains(message)
    }
}