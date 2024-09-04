package com.typewritermc.combatlogx.entries.event

import com.github.sirblobman.combatlogx.api.event.PlayerUntagEvent
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.EventEntry
import org.bukkit.entity.Player

@Entry("on_player_exit_combat", "When a player is no longer in combat", Colors.YELLOW, "fa6-solid:heart-circle-plus")
/**
 * The `Player Exit Combat Event` is fired when a player exits combat.
 *
 * ## How could this be used?
 *
 * This could be used to trigger a message when a player exits combat. Or count the number of times a player exits combat in a certain area and change the behavior of NPCs.
 */
class PlayerExitCombatEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : EventEntry

@EntryListener(PlayerExitCombatEventEntry::class)
fun onExitCombat(event: PlayerUntagEvent, query: Query<PlayerExitCombatEventEntry>) {
    val player: Player = event.player

    query.find() triggerAllFor player
}

