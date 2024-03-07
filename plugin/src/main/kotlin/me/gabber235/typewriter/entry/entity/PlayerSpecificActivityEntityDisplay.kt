package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.AudienceFilter
import me.gabber235.typewriter.entry.entries.AudienceFilterEntry
import me.gabber235.typewriter.entry.entries.PropertySupplier
import me.gabber235.typewriter.entry.entries.TickableDisplay
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PlayerSpecificActivityEntityDisplay(
    ref: Ref<out AudienceFilterEntry>,
    override val creator: EntityCreator,
    private val activityCreators: List<ActivityCreator>,
    private val suppliers: List<Pair<PropertySupplier<*>, Int>>,
    private val spawnLocation: Location,
) : AudienceFilter(ref), TickableDisplay, ActivityEntityDisplay {
    private val activityManagers = ConcurrentHashMap<UUID, ActivityManager>()
    private val entities = ConcurrentHashMap<UUID, DisplayEntity>()

    override fun filter(player: Player): Boolean {
        val activityManager = activityManagers[player.uniqueId] ?: return false
        val npcLocation = activityManager.location
        val distance = npcLocation.distanceSquared(player.location) ?: return false
        return distance <= entityShowRange * entityShowRange
    }

    override fun onPlayerAdd(player: Player) {
        activityManagers.computeIfAbsent(player.uniqueId) {
            ActivityManager(activityCreators.map { it.create(player) }, spawnLocation)
        }
        super.onPlayerAdd(player)
    }

    override fun onPlayerFilterAdded(player: Player) {
        super.onPlayerFilterAdded(player)
        val activityManager = activityManagers[player.uniqueId] ?: return
        entities.computeIfAbsent(player.uniqueId) {
            DisplayEntity(player, creator, activityManager, suppliers.into())
        }
    }

    override fun tick() {
        consideredPlayers.forEach { it.refresh() }

        activityManagers.values.forEach { it.tick() }
        entities.values.forEach { it.tick() }
    }

    override fun onPlayerFilterRemoved(player: Player) {
        super.onPlayerFilterRemoved(player)
        entities.remove(player.uniqueId)?.dispose()
    }

    override fun onPlayerRemove(player: Player) {
        super.onPlayerRemove(player)
        activityManagers.remove(player.uniqueId)?.dispose()
    }

    override fun dispose() {
        super.dispose()
        entities.values.forEach { it.dispose() }
        entities.clear()
        activityManagers.values.forEach { it.dispose() }
        activityManagers.clear()
    }

    override fun playerHasEntity(playerId: UUID, entityId: Int): Boolean {
        return entities[playerId]?.contains(entityId) ?: false
    }
}