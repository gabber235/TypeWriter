package com.typewritermc.combatlogx.entries.event

import com.github.sirblobman.combatlogx.api.event.PlayerTagEvent
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.EventEntry
import org.bukkit.entity.Player

@Entry("on_player_enter_combat", "When a player enters combat", Colors.YELLOW, "fa6-solid:heart-crack")
/**
 * The `Player Enter Combat Event` is triggered when a player enters combat with another player.
 *
 * ## How could this be used?
 *
 * This could be used to play a sound effect when a player enters combat with another player.
 */
class PlayerEnterCombatEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("Triggers to fire for the aggressor who made the attack.")
    val aggressorTriggers: List<Ref<TriggerableEntry>> = emptyList()
) : EventEntry

@EntryListener(PlayerEnterCombatEventEntry::class)
fun onEnterCombat(event: PlayerTagEvent, query: Query<PlayerEnterCombatEventEntry>) {
    val player = event.player
    val aggressor = event.enemy

    val entries = query.find()

    entries triggerAllFor player

    if (aggressor is Player) {
        entries.flatMap { it.aggressorTriggers } triggerEntriesFor aggressor
    }
}

