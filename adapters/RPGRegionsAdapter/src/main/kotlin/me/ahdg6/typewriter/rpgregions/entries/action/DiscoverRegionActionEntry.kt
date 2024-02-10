package me.ahdg6.typewriter.rpgregions.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import net.islandearth.rpgregions.api.RPGRegionsAPI
import net.islandearth.rpgregions.managers.data.region.WorldDiscovery
import org.bukkit.entity.Player
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entry("discover_rpg_region", "Create a discover for an RPGRegions region", Colors.RED, "fa6-solid:address-book")
/**
 * The `Discover Region Action` is used to add a discovery into a user's account.
 *
 * ## How could this be used?
 *
 * This action could be used to reward the player for completing a task/quest. For example, there could exist a quest where an NPC asks for help from the player in exchange for their knowledge of the whereabouts of some important location. This action could be used as the reward when the quest is completed.
 */
class DiscoverRegionActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The region to discover.")
    // The region to discover. Make sure that this is the region ID, not the region's display name.
    private val region: String = "",
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)
        val region = RPGRegionsAPI.getAPI().managers.regionsCache.getConfiguredRegion(region)
        if (!region.isPresent) return

        val user = RPGRegionsAPI.getAPI().managers.storageManager.getAccount(player.uniqueId)
        val date: LocalDateTime = LocalDateTime.now()
        val format: DateTimeFormatter =
            DateTimeFormatter.ofPattern(RPGRegionsAPI.getAPI().config.getString("settings.server.discoveries.date.format"))

        val formattedDate = date.format(format)
        val worldDiscovery = WorldDiscovery(formattedDate, region.get().id)
        user.get().addDiscovery(worldDiscovery)

        RPGRegionsAPI.getAPI().managers.storageManager.removeCachedAccount(player.uniqueId)
    }
}