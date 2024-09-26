package com.typewritermc.superiorskyblock.entries.fact

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.bgsoftware.superiorskyblock.api.island.Island
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import org.bukkit.entity.Player
import kotlin.math.roundToInt


enum class IslandFacts(private val retrieveFact: (SuperiorPlayer, Island) -> Int) {
    LEVEL({ _, island -> island.islandLevel.intValueExact() }),
    RAW_LEVEL({ _, island -> island.rawLevel.intValueExact() }),
    BONUS_LEVEL({ _, island -> island.bonusLevel.intValueExact() }),
    ISLAND_BANK_BALANCE({ _, island -> island.islandBank.balance.intValueExact() }),
    WORTH({ _, island -> island.worth.intValueExact() }),
    RAW_WORTH({ _, island -> island.rawWorth.intValueExact() }),
    BONUS_WORTH({ _, island -> island.bonusWorth.intValueExact() }),
    PLAYER_RATING({ player, island -> island.getRating(player).value }),
    TOTAL_RATING({ _, island -> island.totalRating.roundToInt() }),
    RATING_AMOUNT({ _, island -> island.ratingAmount }),
    COOP_PLAYERS_AMOUNT({ _, island -> island.coopPlayers.size }),
    ;

    fun getFact(player: SuperiorPlayer, island: Island): Int {
        return retrieveFact(player, island)
    }
}

@Entry("island_fact", "Various facts about a player's island", Colors.PURPLE, "fa6-solid:map-location-dot")
/**
 * A [fact](/docs/creating-stories/facts) that can retrieve various information about an island.
 *
 * <fields.ReadonlyFactInfo />
 *
 * Be aware that this fact will return -1 if the player is not in an island.
 *
 * ## How could this be used?
 *
 * This fact could be used to get the island's level and only allow some actions if the island is a certain level.
 */
class IslandFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    val fact: IslandFacts = IslandFacts.LEVEL,
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island ?: return FactData(-1)
        val value = fact.getFact(sPlayer, island)
        return FactData(value)
    }
}