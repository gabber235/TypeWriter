package com.caleb.typewriter.vault.entries.group

import com.caleb.typewriter.vault.VaultAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.CriteriaOperator
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.GroupId
import org.bukkit.entity.Player

@Entry("balance_audience", "Audiences grouped by balance", Colors.MYRTLE_GREEN, "fa6-solid:balance-scale")
/**
 * The `Balance Audience` is an group for which a player's balance meets a certain condition.
 * To determine if a player is part of this group, the balance of the player is checked for each condition.
 * The first condition that is met determines the group the player is part of.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to a player's balance, like a VIPs where all share some balance threshold.
 */
class BalanceGroupEntry(
    override val id: String = "",
    override val name: String = "",
    val group: List<BalanceGroup> = emptyList(),
) : GroupEntry {
    override fun groupId(player: Player): GroupId? {
        val balance = VaultAdapter.economy?.getBalance(player) ?: return null
        return group
            .firstOrNull { it.operator.isValid(balance, it.value) }
            ?.let { GroupId(it.audience) }
    }
}

data class BalanceGroup(
    val audience: String = "",
    val operator: CriteriaOperator = CriteriaOperator.GREATER_THAN,
    val value: Double = 0.0,
)