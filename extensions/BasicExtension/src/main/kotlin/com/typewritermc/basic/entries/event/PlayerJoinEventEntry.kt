package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.EventEntry
import org.bukkit.event.player.PlayerJoinEvent

@Entry("on_player_join", "When the player joins the server", Colors.YELLOW, "fluent:person-add-20-filled")
/**
 * The `Player Join Event` event is called when a player joins the server.
 *
 * ## How could this be used?
 *
 * This could be used with [facts](https://docs.typewritermc.com/docs/creating-stories/facts) to give a new player a welcome message, or welcome back new players.
 * You can also use it to give new players a starting item, or to give them a starting amount of money with the Vault adapter.
 */
class PlayerJoinEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : EventEntry

@EntryListener(PlayerJoinEventEntry::class)
fun onJoin(event: PlayerJoinEvent, query: Query<PlayerJoinEventEntry>) {
    query.find().triggerAllFor(event.player, context())
}