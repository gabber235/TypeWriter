package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.EntryListener
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.triggerAllFor
import me.gabber235.typewriter.utils.Icons
import org.bukkit.event.player.PlayerJoinEvent

@Entry("on_player_join", "When the player joins the server", Colors.YELLOW, Icons.PERSON_CIRCLE_PLUS)
/**
 * The `Player Join Event` event is called when a player joins the server.
 *
 * ## How could this be used?
 *
 * This could be used with [facts](/docs/facts) to give a new player a welcome message, or welcome back new players. You can also use it to give new players a starting item, or to give them a starting amount of money with the [Vault adapter](/adapters/VaultAdapter).
 */
class PlayerJoinEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(PlayerJoinEventEntry::class)
fun onJoin(event: PlayerJoinEvent, query: Query<PlayerJoinEventEntry>) {
    println("Player ${event.player.name} joined the server: ${query.find()}")
    query.find() triggerAllFor event.player
}