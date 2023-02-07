package me.gabber235.typewriter.entries.event

import io.papermc.paper.event.player.AsyncChatEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.plainText

@Entry("on_chat_message", "When a player sends a message in chat", Colors.YELLOW, Icons.SOLID_MESSAGE)
class ChatMessageEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val regex: String = "",
) : EventEntry


@EntryListener(ChatMessageEventEntry::class)
fun onChatMessage(event: AsyncChatEvent, query: Query<ChatMessageEventEntry>) {
	val message = event.message().plainText()
	query findWhere {
		it.regex.toRegex().matches(message)
	} triggerAllFor event.player
}