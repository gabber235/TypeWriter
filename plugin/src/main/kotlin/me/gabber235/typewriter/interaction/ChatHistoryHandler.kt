package me.gabber235.typewriter.interaction

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.*
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

object ChatHistoryHandler :
	PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.SYSTEM_CHAT) {

	fun init() {
		ProtocolLibrary.getProtocolManager().addPacketListener(this)
	}

	private val histories = mutableMapOf<UUID, ChatHistory>()

	private val contentMethod by memoized<Class<out Any>, Method> { it.getMethod("content") }
	private val adventureContentMethod by memoized<Class<out Any>, Method> {
		it.getMethod("adventure\$content")
	}
	private val isActionBarMessageMethod by memoized<Class<out Any>, Method?> { clazz ->
		clazz.methods.find {
			it.returnType == Boolean::class.java && it.parameterCount == 0
		}
	}

	// When the serer sends a message to the player
	override fun onPacketSending(event: PacketEvent) {
		if (event.packetType == PacketType.Play.Server.SYSTEM_CHAT) {
			val handle = event.packet.handle
			val isActionBarMessage = isActionBarMessageMethod(handle::class.java)?.invoke(handle) as? Boolean ?: false
			if (isActionBarMessage) return

			val contentValue =
				contentMethod(handle::class.java).invoke(handle) ?: adventureContentMethod(handle::class.java).invoke(
					handle
				) ?: return

			val content = contentValue as? String ?: return
			val component = GsonComponentSerializer.gson().deserialize(content)
			// If the message is a broadcast of previous messages.
			// We don't want to add this to the history.
			if (component is TextComponent && component.content() == "no-index") return
			getHistory(event.player).addMessage(component)
		}
	}

	fun getHistory(player: Player): ChatHistory {
		return histories.getOrPut(player.uniqueId) { ChatHistory() }
	}

	fun shutdown() {
		ProtocolLibrary.getProtocolManager().removePacketListener(this)
	}
}

val Player.chatHistory: ChatHistory
	get() = ChatHistoryHandler.getHistory(this)

class ChatHistory {
	private val messages = ConcurrentLinkedQueue<Component>()

	fun addMessage(message: Component) {
		messages.add(message)
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
		messages.forEach { msg = msg.append(Component.text("\n")).append(it) }
		player.sendMessage(msg)
	}

	fun composeDarkMessage(message: Component, clear: Boolean = true): Component {
		// Start with "no-index" to prevent the server from adding the message to the history
		var msg = Component.text("no-index")
		if (clear) msg = msg.append(Component.text(clearMessage()))
		messages.forEach {
			msg = msg.append("<#7d8085>${it.plainText()}</#7d8085>\n".asMini())
		}
		return msg.append(message)
	}
}