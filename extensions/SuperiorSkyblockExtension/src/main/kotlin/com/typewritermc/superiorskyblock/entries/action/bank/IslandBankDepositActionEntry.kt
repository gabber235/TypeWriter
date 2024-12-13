package com.typewritermc.superiorskyblock.entries.action.bank

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.math.BigDecimal

@Entry("island_bank_deposit", "Deposit into a player's Island bank", Colors.RED, "fa6-solid:piggy-bank")
/**
 * The `Island Bank Deposit Action` is used to deposit money into the player's Island bank.
 *
 * ## How could this be used?
 *
 * This could be used to reward players for completing a challenge or quest.
 *
 */
class IslandBankDepositActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val amount: Double = 0.0
) : ActionEntry {
    override fun ActionTrigger.execute() {
        val amountConverted: BigDecimal = BigDecimal.valueOf(amount)

        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island
        island?.islandBank?.depositAdminMoney(Bukkit.getServer().consoleSender, amountConverted)
    }
}