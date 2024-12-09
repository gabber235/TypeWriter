package com.typewritermc.vault.entries.fact

import com.typewritermc.vault.VaultInitializer
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent

@Entry("balance_fact", "The balance of a player's account", Colors.PURPLE, "fa6-solid:money-bill-wave")
/**
 * A [fact](/docs/creating-stories/facts) that represents a player's balance.
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
        val economyHandler = KoinJavaComponent.get<VaultInitializer>(VaultInitializer::class.java).economy ?: return FactData(0)
        val balance = economyHandler.getBalance(player)
        return FactData(balance.toInt())
    }
}