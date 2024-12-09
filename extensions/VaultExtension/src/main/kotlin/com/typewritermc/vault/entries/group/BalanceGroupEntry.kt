package com.typewritermc.vault.entries.group

import com.google.gson.annotations.SerializedName
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Default
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.GroupId
import com.typewritermc.vault.VaultInitializer
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent

@Entry("balance_audience", "Audiences grouped by balance", Colors.MYRTLE_GREEN, "streamline:justice-scale-2-solid")
/**
 * The `Balance Audience` is an group for which a player's balance meets a certain condition.
 * To determine if a player is part of this group, the balance of the player is checked for each condition.
 * The first condition that is met determines the group the player is part of.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to a player's balance, like a VIPs where all share some balance threshold.
 */
class BalanceGroupEntry(
    override val id: String = "",
    override val name: String = "",
    val group: List<BalanceGroup> = emptyList(),
) : GroupEntry {
    override fun groupId(player: Player): GroupId? {
        val balance = KoinJavaComponent.get<VaultInitializer>(VaultInitializer::class.java).economy?.getBalance(player)
            ?: return null
        return group
            .firstOrNull { it.operator.isValid(balance, it.value) }
            ?.let { GroupId(it.audience) }
    }
}

data class BalanceGroup(
    val audience: String = "",
    @Default("\">=\"")
    val operator: Operator = Operator.GREATER_THAN,
    val value: Double = 0.0,
)

enum class Operator {
    @SerializedName("==")
    EQUALS,

    @SerializedName("<")
    LESS_THAN,

    @SerializedName(">")
    GREATER_THAN,

    @SerializedName("<=")
    LESS_THAN_OR_EQUALS,

    @SerializedName(">=")
    GREATER_THAN_OR_EQUAL,

    @SerializedName("!=")
    NOT_EQUALS

    ;

    fun isValid(value: Double, criteria: Double): Boolean {
        return when (this) {
            EQUALS -> value == criteria
            LESS_THAN -> value < criteria
            GREATER_THAN -> value > criteria
            LESS_THAN_OR_EQUALS -> value <= criteria
            GREATER_THAN_OR_EQUAL -> value >= criteria
            NOT_EQUALS -> value != criteria
        }
    }
}
