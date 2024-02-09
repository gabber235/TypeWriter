package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceId
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("global_audience", "One audience with all the online players", Colors.MYRTLE_GREEN, Icons.GLOBE)
/**
 * The `Global Audience` is an audience that includes all the online players.
 *
 * ## How could this be used?
 * This could be used to have facts that are the same for all the players,
 * like a global objective that all the players have to work together to achieve.
 */
class GlobalAudienceEntry(
    override val id: String = "",
    override val name: String = "",
) : AudienceEntry {
    override fun audienceId(player: Player): AudienceId = AudienceId("global")
}