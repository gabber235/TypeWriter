package com.caleb.typewriter.superiorskyblock.entries.audience

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.AudienceId
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry("island_audience", "Audience for the whole island", Colors.MYRTLE_GREEN, Icons.GLOBE)
class IslandAudienceEntry(
    override val id: String = "",
    override val name: String = "",
) : AudienceEntry {
    override fun audienceId(player: Player): AudienceId? {
        val sPlayer = SuperiorSkyblockAPI.getPlayer(player)
        val island = sPlayer.island ?: return null
        return AudienceId(island.uniqueId)
    }
}