package com.caleb.typewriter.vault.entries.fact

import com.caleb.typewriter.vault.VaultAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.FactData
import org.bukkit.entity.Player

@Entry("balance_fact", "The balance of a player's account", Colors.PURPLE, "fa6-solid:money-bill-wave")
/**
 * A [fact](/docs/facts) that represents a player's balance.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 *
 * This fact could be used to track a player's balance in a game. For example, if the player is rich, allow them to access to a VIP area. If the player is poor, they can't afford to enter.
 */
class BalanceFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val permissionHandler = VaultAdapter.economy ?: return FactData(0)
        val balance = permissionHandler.getBalance(player)
        return FactData(balance.toInt())
    }
}