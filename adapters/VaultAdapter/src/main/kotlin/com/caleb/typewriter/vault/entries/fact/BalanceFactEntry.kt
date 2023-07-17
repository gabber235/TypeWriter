package com.caleb.typewriter.vault.entries.fact

import com.caleb.typewriter.vault.VaultAdapter
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import java.util.*

@Entry("balance_fact", "The balance of a player's account", Colors.PURPLE, Icons.MONEY_BILLS)
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
) : ReadableFactEntry {
    override fun read(playerId: UUID): Fact {
        val permissionHandler = VaultAdapter.economy ?: return Fact(id, 0)
        val balance = permissionHandler.getBalance(server.getOfflinePlayer(playerId))
        return Fact(id, balance.toInt())
    }
}