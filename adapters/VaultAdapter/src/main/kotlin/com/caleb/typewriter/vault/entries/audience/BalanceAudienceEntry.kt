package com.caleb.typewriter.vault.entries.audience

import com.caleb.typewriter.vault.VaultAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.CriteriaOperator
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceId
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("balance_audience", "Audiences grouped by balance", Colors.MYRTLE_GREEN, Icons.MONEY_BILLS)
/**
 * The `Balance Audience` is an audience for which a player's balance meets a certain condition.
 * To determine if a player is part of this audience, the balance of the player is checked for each condition.
 * The first condition that is met determines the audience the player is part of.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to a player's balance, like a VIPs where all share some balance threshold.
 */
class BalanceAudienceEntry(
    override val id: String = "",
    override val name: String = "",
    val audiences: List<BalanceAudience> = emptyList(),
) : AudienceEntry {
    override fun audienceId(player: Player): AudienceId? {
        val balance = VaultAdapter.economy?.getBalance(player) ?: return null
        return audiences
            .firstOrNull { it.operator.isValid(balance, it.value) }
            ?.let { AudienceId(it.audience) }
    }
}

data class BalanceAudience(
    val audience: String = "",
    val operator: CriteriaOperator = CriteriaOperator.GREATER_THAN,
    val value: Double = 0.0,
)