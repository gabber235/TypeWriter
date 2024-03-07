package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.AudienceFilter
import me.gabber235.typewriter.entry.entries.AudienceFilterEntry
import me.gabber235.typewriter.entry.entries.PropertySupplier
import me.gabber235.typewriter.entry.entries.TickableDisplay
import me.gabber235.typewriter.utils.config
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

val entityShowRange by config("entity.show-range", 50.0, "The range at which entities are shown")

class CommonActivityEntityDisplay(
    ref: Ref<out AudienceFilterEntry>,
    override val creator: EntityCreator,
    private val activityCreators: List<ActivityCreator>,
    private val suppliers: List<Pair<PropertySupplier<*>, Int>>,
    private val spawnLocation: Location,
) : AudienceFilter(ref), TickableDisplay, ActivityEntityDisplay {
    private var activityManager: ActivityManager? = null
    private val entities = ConcurrentHashMap<UUID, DisplayEntity>()

    override fun filter(player: Player): Boolean {
        val npcLocation = activityManager?.location ?: return false
        val distance = npcLocation.distanceSquared(player.location) ?: return false
        return distance <= entityShowRange * entityShowRange
    }

    override fun initialize() {
        super.initialize()
        activityManager = ActivityManager(activityCreators.map { it.create(null) }, spawnLocation)
    }

    override fun onPlayerFilterAdded(player: Player) {
        super.onPlayerFilterAdded(player)
        val activityManager = activityManager ?: return
        entities.computeIfAbsent(player.uniqueId) {
            DisplayEntity(player, creator, activityManager, suppliers.into())
        }
    }

    override fun tick() {
        consideredPlayers.forEach { it.refresh() }

        activityManager?.tick()
        entities.values.forEach { it.tick() }
    }

    override fun onPlayerFilterRemoved(player: Player) {
        super.onPlayerFilterRemoved(player)
        entities.remove(player.uniqueId)?.dispose()
    }

    override fun dispose() {
        super.dispose()
        entities.values.forEach { it.dispose() }
        entities.clear()
        activityManager?.dispose()
        activityManager = null
    }

    override fun playerHasEntity(playerId: UUID, entityId: Int): Boolean {
        return entities[playerId]?.contains(entityId) ?: false
    }
}
