package me.gabber235.typewriter.interaction

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.reflect.StructureModifier
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import lirand.api.extensions.server.server
import me.gabber235.typewriter.utils.plainText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
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

class ChatHistoryHandler(plugin: Plugin) :
    PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.SYSTEM_CHAT), Listener {

    fun initialize() {
        ProtocolLibrary.getProtocolManager().addPacketListener(this)
        server.pluginManager.registerSuspendingEvents(this, plugin)
    }

    private val histories = mutableMapOf<UUID, ChatHistory>()

    // When the serer sends a message to the player
    override fun onPacketSending(event: PacketEvent) {
        if (event.packetType != PacketType.Play.Server.SYSTEM_CHAT) return

        if (event.packet.isActionBar()) return

        val component = event.packet.getChatComponent() ?: return

        // If the message is a broadcast of previous messages.
        // We don't want to add this to the history.
        if (component is TextComponent && component.content() == "no-index") return
        val history = getHistory(event.player)
        history.addMessage(component)

        if (history.isBlocking()) {
            event.isCancelled = true
        }
    }


    fun getHistory(player: Player): ChatHistory {
        return histories.getOrPut(player.uniqueId) { ChatHistory() }
    }

    fun blockMessages(player: Player) {
        getHistory(player).startBlocking()
    }

    fun unblockMessages(player: Player) {
        getHistory(player).stopBlocking()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(event: PlayerQuitEvent) {
        histories.remove(event.player.uniqueId)
    }

    fun shutdown() {
        ProtocolLibrary.getProtocolManager().removePacketListener(this)
    }
}

/**
 * Returns if a SystemChat packet is sent as an action bar.
 */
fun PacketContainer.isActionBar(): Boolean {
    val booleans = booleans
    if (booleans.size() > 0) {
        return booleans.readSafely(0)
    }
    return integers.readSafely(0) == 2
}

/**
 * Returns the chat component from a SystemChat packet.
 */
fun PacketContainer.getChatComponent(): Component? {
    // Version 1.20.4+
    if (chatComponents.size() > 0) {
        val wrapped = chatComponents.readSafely(0)
        val json = wrapped.json
        return GsonComponentSerializer.gson().deserialize(json)
    }
    // Version 1.20-1.20.3
    val adventureModifier: StructureModifier<Component>? = getSpecificModifier(Component::class.java)
    val component = adventureModifier?.readSafely(0)
    if (component != null) {
        return component
    }
    // Version 1.19-1.19.4
    return strings.readSafely(0)?.let { GsonComponentSerializer.gson().deserialize(it) }
}

val Player.chatHistory: ChatHistory
    get() = get<ChatHistoryHandler>(ChatHistoryHandler::class.java).getHistory(this)

fun Player.startBlockingMessages() = chatHistory.startBlocking()
fun Player.stopBlockingMessages() = chatHistory.stopBlocking()

class ChatHistory {
    private val messages = ConcurrentLinkedQueue<OldMessage>()
    private var blocking: Boolean = false

    fun startBlocking() {
        blocking = true
    }

    fun stopBlocking() {
        blocking = false
    }

    fun isBlocking(): Boolean = blocking

    fun addMessage(message: Component) {
        messages.add(OldMessage(message))
        while (messages.size > 100) {
            messages.poll()
        }
    }

    fun clear() {
        messages.clear()
    }

    private fun clearMessage() = "\n".repeat(100 - messages.size)

    fun resendMessages(player: Player, clear: Boolean = true) {
        // Start with "no-index" to prevent the server from adding the message to the history
        var msg = Component.text("no-index")
        if (clear) msg = msg.append(Component.text(clearMessage()))
        messages.forEach { msg = msg.append(Component.text("\n")).append(it.message) }
        player.sendMessage(msg)
    }

    fun composeDarkMessage(message: Component, clear: Boolean = true): Component {
        // Start with "no-index" to prevent the server from adding the message to the history
        var msg = Component.text("no-index")
        if (clear) msg = msg.append(Component.text(clearMessage()))
        messages.forEach {
            msg = msg.append(it.darkenMessage)
        }
        return msg.append(message)
    }

    fun composeEmptyMessage(message: Component, clear: Boolean = true): Component {
        // Start with "no-index" to prevent the server from adding the message to the history
        var msg = Component.text("no-index")
        if (clear) msg = msg.append(Component.text(clearMessage()))
        return msg.append(message)
    }
}

data class OldMessage(val message: Component) {
    val darkenMessage: Component by lazy(LazyThreadSafetyMode.NONE) {
        Component.text("${message.plainText()}\n").color(TextColor.color(0x7d8085))
    }
}