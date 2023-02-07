package me.gabber235.typewriter.entries.event

import io.papermc.paper.event.player.AsyncChatEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import me.gabber235.typewriter.utils.plainText
import java.util.*

@Entry(
	"on_message_contains_text",
	"When the player sends a chat message containing certain text",
	Colors.YELLOW,
	Icons.NOTE_STICKY
)
class ChatContainsTextEventEntry(
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val text: String = "",
	val exactSame: Boolean = false
) : EventEntry


@EntryListener(ChatContainsTextEventEntry::class)
fun onChat(event: AsyncChatEvent, query: Query<ChatContainsTextEventEntry>) {
	val message = event.message().plainText()
	query findWhere {
		if(it.exactSame)
			Regex(it.text).matches(message)
		else
			Regex(it.text).containsMatchIn(message)
	} triggerAllFor event.player
}