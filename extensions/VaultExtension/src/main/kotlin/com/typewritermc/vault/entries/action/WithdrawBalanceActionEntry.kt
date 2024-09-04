package com.typewritermc.vault.entries.action

import com.typewritermc.vault.VaultInitializer
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
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

        val economy: Economy = VaultInitializer.economy ?: return

        economy.withdrawPlayer(player, amount)
    }
}