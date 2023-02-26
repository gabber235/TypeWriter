package com.caleb.typewriter.superiorskyblock.entries.action.bank

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.caleb.typewriter.superiorskyblock.SuperiorSkyblockAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.math.BigDecimal

@Entry("superiorskyblock_withdraw_bank", "[SuperiorSkyblock] Withdraw into a player's Island bank", Colors.RED, Icons.PIGGY_BANK)
data class IslandBankWithdrawActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    val amount: Double = 0.0

) : ActionEntry {

    override fun execute(player: Player) {
        super.execute(player)

        val amountConverted: BigDecimal = BigDecimal.valueOf(amount)

        var sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        var island = sPlayer.getIsland()
        island?.islandBank?.withdrawAdminMoney(Bukkit.getServer().consoleSender, amountConverted)

    }

}