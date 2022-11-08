package me.gabber235.typewriter.interaction

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.*
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

object ChatHistoryHandler :
	PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.SYSTEM_CHAT) {

	private val histories = mutableMapOf<UUID, ChatHistory>()

	// When the serer sends a message to the player
	override fun onPacketSending(event: PacketEvent) {
		if (event.packetType == PacketType.Play.Server.SYSTEM_CHAT) {
			val handle = event.packet.handle
			val method = handle::class.java.getMethod("content")
			val content = method.invoke(handle) as? String
			if (content == null) {
				plugin.logger.warning("Could not get content from packet")
				return
			}
			val component = GsonComponentSerializer.gson().deserialize(content)
			if (component is TextComponent && component.content() == "no-index") return
			getHistory(event.player).addMessage(component)
		}
	}

	fun getHistory(player: Player): ChatHistory {
		return histories.getOrPut(player.uniqueId) { ChatHistory() }
	}
}

val Player.chatHistory: ChatHistory
	get() = ChatHistoryHandler.getHistory(this)

class ChatHistory {
	private val messages = ConcurrentLinkedQueue<Component>()

	fun addMessage(message: Component) {
		messages.add(message)
		while (messages.size > 80) {
			messages.poll()
		}
	}

	fun clear() {
		messages.clear()
	}

	private fun clearMessage() = "\n".repeat(80 - messages.size)

	fun resendMessages(player: Player, clear: Boolean = true) {
		var msg = Component.text("no-index")
		if (clear) msg = msg.append(clearMessage().asMini())
		messages.forEach { msg = msg.append(Component.text("\n")).append(it) }
		player.sendMessage(msg)
	}

	fun composeDarkMessage(message: Component, clear: Boolean = true): Component {
		var msg = Component.text("no-index")
		if (clear) msg = msg.append(clearMessage().asMini())
		messages.forEach {
			msg = msg.append("<#7d8085>${it.plainText()}</#7d8085>\n".asMini())
		}
		return msg.append(message)
	}
}