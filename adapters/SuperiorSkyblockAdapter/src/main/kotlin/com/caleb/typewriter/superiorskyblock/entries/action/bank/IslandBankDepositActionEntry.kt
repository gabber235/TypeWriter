package com.caleb.typewriter.superiorskyblock.entries.action.bank

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
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
    @Help("The amount to deposit into the player's Island bank")
    val amount: Double = 0.0
) : ActionEntry {

    override fun execute(player: Player) {
        super.execute(player)

        val amountConverted: BigDecimal = BigDecimal.valueOf(amount)

        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island
        island?.islandBank?.depositAdminMoney(Bukkit.getServer().consoleSender, amountConverted)
    }
}