package com.caleb.typewriter.vault.entries.action

import com.caleb.typewriter.vault.VaultAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player

@Entry("withdraw_balance", "Withdraw Balance", Colors.RED, "majesticons:money-minus")
/**
 * The `Withdraw Balance Action` is used to withdraw money from a user's balance.
 *
 * ## How could this be used?
 *
 * This action could be used to withdraw money from a user's balance if they lose a bet, or get killed.
 */
class WithdrawBalanceActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The amount of money to withdraw.")
    private val amount: Double = 0.0,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val economy: Economy = VaultAdapter.economy ?: return

        economy.withdrawPlayer(player, amount)
    }
}