package com.typewritermc.vault.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.vault.VaultInitializer
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent

@Entry("deposit_balance", "Deposit Balance", Colors.RED, "majesticons:money-plus")
/**
 * The `Deposit Balance Action` is used to deposit money into a user's balance.
 *
 * ## How could this be used?
 *
 * This action could be used to reward the player for completing a task/quest.
 */
class DepositBalanceActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    private val amount: Var<Double> = ConstVar(0.0),
) : ActionEntry {
    override fun ActionTrigger.execute() {
        val economy: Economy = KoinJavaComponent.get<VaultInitializer>(VaultInitializer::class.java).economy ?: return

        economy.depositPlayer(player, amount.get(player, context))
    }
}