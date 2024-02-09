package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceId
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("player_audience", "Audience for every individual player", Colors.MYRTLE_GREEN, Icons.USER)
/**
 * The `Player Audience` is an audience that is specific to each individual player.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to each player, like their progress in a quest or their reputation with a faction.
 */
class PlayerAudienceEntry(
    override val id: String = "",
    override val name: String = "",
) : AudienceEntry {
    override fun audienceId(player: Player): AudienceId = AudienceId(player.uniqueId)
}