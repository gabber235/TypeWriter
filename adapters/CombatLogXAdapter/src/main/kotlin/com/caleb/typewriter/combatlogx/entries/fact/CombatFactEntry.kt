package com.caleb.typewriter.combatlogx.entries.fact

import com.caleb.typewriter.combatlogx.CombatLogXAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.FactData
import org.bukkit.entity.Player

@Entry("combat_fact", "If the player is in combat", Colors.PURPLE, "fa6-solid:shield-halved")
/**
 * A [fact](/docs/facts) that tells whether a player is in combat.
 *
 * <fields.ReadonlyFactInfo/>
 *
 * ## How could this be used?
 *
 * This could be used to disable certain actions when the player is in combat.
 */
class CombatFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val combatLogger = CombatLogXAdapter.getAPI() ?: return FactData(0)
        val value = if (combatLogger.combatManager.isInCombat(player)) 1 else 0

        return FactData(value)
    }
}