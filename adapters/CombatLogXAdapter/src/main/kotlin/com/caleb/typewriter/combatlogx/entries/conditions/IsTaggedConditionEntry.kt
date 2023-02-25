package com.caleb.typewriter.combatlogx.entries.conditions

import com.caleb.typewriter.combatlogx.CombatLogXAdapter
import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.interaction.InteractionHandler
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("is_combat_tagged", "[CombatLogX] Continues if a player meets tag requirement", Colors.PINK, Icons.FILTER)
class IsTaggedConditionEntry(
    override val id: String,
    override val name: String,
    @SerializedName("triggers")
    @Triggers
    @EntryIdentifier(TriggerableEntry::class)
    val nextTriggers: List<String> = emptyList(),
    override val criteria: List<Criteria>,
    override val modifiers: List<Modifier>,
    private var isTagged: Boolean = false,

    ) : ActionEntry {

    override val triggers: List<String>
        get() = emptyList()

    override fun execute(player: Player) {

        val api = CombatLogXAdapter.getAPI() ?: return
        val combatManager = api.combatManager ?: return

        if(combatManager.isInCombat(player)) {
            if(isTagged) {
                nextTriggers.map { s -> EntryTrigger(s) }.let { triggers ->
                    InteractionHandler.startInteractionAndTrigger(player, triggers)
                }
            }
        } else {
            if(!isTagged) {
                nextTriggers.map { s -> EntryTrigger(s) }.let { triggers ->
                    InteractionHandler.startInteractionAndTrigger(player, triggers)
                }
            }
        }

    }
}