package com.typewritermc.vault.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.vault.VaultInitializer
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player

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
    private val amount: Double = 0.0,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val economy: Economy = VaultInitializer.economy ?: return

        economy.depositPlayer(player, amount)
    }
}