package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceId
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("world_audience", "Audience for the whole world", Colors.MYRTLE_GREEN, Icons.GLOBE)
/**
 * The `World Audience` is an audience that includes all the players in a world.
 *
 * ## How could this be used?
 * This could be used to have facts that are specific to a world, like the state of a world event or the state of a world boss.
 */
class WorldAudienceEntry(
    override val id: String = "",
    override val name: String = "",
) : AudienceEntry {
    override fun audienceId(player: Player): AudienceId = AudienceId(player.world.name)
}