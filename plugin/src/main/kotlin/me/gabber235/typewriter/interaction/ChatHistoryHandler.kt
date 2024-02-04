package me.gabber235.typewriter.interaction

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.event.PacketListenerAbstract
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import lirand.api.extensions.server.server
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.plainText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.koin.java.KoinJavaComponent.get
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class ChatHistoryHandler :
    PacketListenerAbstract(PacketListenerPriority.HIGH), Listener {

    fun initialize() {
        PacketEvents.getAPI().eventManager.registerListener(this)
        server.pluginManager.registerSuspendingEvents(this, plugin)
    }

    private val histories = mutableMapOf<UUID, ChatHistory>()

    // When the serer sends a message to the player
    override fun onPacketSend(event: PacketSendEvent?) {
        if (event == null) return
        if (event.packetType != PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) return

        val packet = WrapperPlayServerSystemChatMessage(event)
        if (packet.isOverlay) return

        val component = packet.message
        if (component is TextComponent && component.content() == "no-index") return
        val history = getHistory(event.user.uuid)
        history.addMessage(component)

        if (history.isBlocking()) {
            event.isCancelled = true
        }
    }

    fun getHistory(pid: UUID): ChatHistory {
        return histories.getOrPut(pid) { ChatHistory() }
    }

    fun getHistory(player: Player): ChatHistory = getHistory(player.uniqueId)

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
        PacketEvents.getAPI().eventManager.unregisterListener(this)
    }
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