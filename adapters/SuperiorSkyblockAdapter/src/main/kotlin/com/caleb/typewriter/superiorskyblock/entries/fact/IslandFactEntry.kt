package com.caleb.typewriter.superiorskyblock.entries.fact

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.bgsoftware.superiorskyblock.api.island.Island
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.Icons
import java.util.*
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

@Entry("island_fact", "Various facts about a player's island", Colors.PURPLE, Icons.MAP_LOCATION_DOT)
class IslandFactEntry(
	override val id: String = "",
	override val name: String = "",
	override val comment: String = "",
	@Help("The fact to get")
	val fact: IslandFacts,
) : ReadableFactEntry {
	override fun read(playerId: UUID): Fact {
		val sPlayer = SuperiorSkyblockAPI.getPlayer(playerId)
		val island = sPlayer.island ?: return Fact(id, -1)
		val value = fact.getFact(sPlayer, island)
		return Fact(id, value)
	}
}