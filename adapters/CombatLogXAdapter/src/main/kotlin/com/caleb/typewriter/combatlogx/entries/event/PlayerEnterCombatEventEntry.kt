package com.caleb.typewriter.combatlogx.entries.event

import com.github.sirblobman.combatlogx.api.event.PlayerTagEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("on_player_enter_combat", "When a player enters combat", Colors.YELLOW, Icons.HEART_CRACK)
class PlayerEnterCombatEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
    @Triggers
    @EntryIdentifier(TriggerableEntry::class)
    @Help("The triggers for the aggressor")
    val aggressorTriggers: List<String> = emptyList()
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

