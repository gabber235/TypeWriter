package com.caleb.typewriter.griefdefender.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import net.islandearth.rpgregions.api.RPGRegionsAPI
import net.islandearth.rpgregions.managers.data.region.WorldDiscovery
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Entry("discover_region", "Create a discover for a RPGRegions region", Colors.RED, Icons.ADDRESS_BOOK)
class DiscoverRegionActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    @Help("The region to discover.")
    private val region: String = "",
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)
        val region = RPGRegionsAPI.getAPI().managers.regionsCache.getConfiguredRegion(region) ?: return
        val user = RPGRegionsAPI.getAPI().managers.storageManager.getAccount(player.uniqueId) ?: return
        val date: LocalDateTime = LocalDateTime.now()
        val format: DateTimeFormatter =
            DateTimeFormatter.ofPattern(RPGRegionsAPI.getAPI().config.getString("settings.server.discoveries.date.format"))

        val formattedDate = date.format(format)
        val worldDiscovery = WorldDiscovery(formattedDate, region.get().id)
        user.get().addDiscovery(worldDiscovery)

        RPGRegionsAPI.getAPI().managers.storageManager.removeCachedAccount(player.uniqueId)
    }
}