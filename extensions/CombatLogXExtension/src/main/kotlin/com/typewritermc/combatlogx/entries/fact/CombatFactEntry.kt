package com.typewritermc.combatlogx.entries.fact

import com.github.sirblobman.combatlogx.api.ICombatLogX
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import org.bukkit.Bukkit
import org.bukkit.entity.Player

@Entry("combat_fact", "If the player is in combat", Colors.PURPLE, "fa6-solid:shield-halved")
/**
 * A [fact](/docs/creating-stories/facts) that tells whether a player is in combat.
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
    private fun combatLogXApi(): ICombatLogX? {
        val pluginManager = Bukkit.getPluginManager()
        val plugin = pluginManager.getPlugin("CombatLogX")
        return plugin as? ICombatLogX
    }
    override fun readSinglePlayer(player: Player): FactData {
        val combatLogger = combatLogXApi() ?: return FactData(0)
        val value = if (combatLogger.combatManager.isInCombat(player)) 1 else 0

        return FactData(value)
    }
}