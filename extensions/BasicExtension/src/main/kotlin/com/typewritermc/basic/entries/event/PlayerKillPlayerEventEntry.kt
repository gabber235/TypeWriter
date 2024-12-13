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
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent

@Entry("on_player_kill_player", "When a player kills a player", Colors.YELLOW, "fa6-solid:skull")
/**
 * The `Player Kill Player Event` is triggered when a player kills another player. If you want to detect when a player kills some thing else, use the [`Player Kill Entity Event`](on_player_kill_entity) instead.
 *
 * ## How could this be used?
 *
 * This could be used to create a bounty system, where someone places a bounty on another player, and when that player is killed, the bounty is paid out to the player who kills them.
 */
class PlayerKillPlayerEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val killedTriggers: List<Ref<TriggerableEntry>> = emptyList(),
) : EventEntry


@EntryListener(PlayerKillPlayerEventEntry::class)
fun onKill(event: EntityDeathEvent, query: Query<PlayerKillPlayerEventEntry>) {
    val killer =
        event.entity.killer ?: return

    val target = event.entity as? Player ?: return

    val entries = query.find()

    entries.triggerAllFor(killer, context())
    entries.flatMap { it.killedTriggers }.triggerEntriesFor(target, context())
}