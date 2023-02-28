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