package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.AudienceFilter
import me.gabber235.typewriter.entry.entries.EntityInstanceEntry
import me.gabber235.typewriter.entry.entries.PropertySupplier
import me.gabber235.typewriter.entry.entries.TickableDisplay
import me.gabber235.typewriter.utils.config
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

val entityShowRange by config("entity.show-range", 50.0, "The range at which entities are shown")

class SharedActivityEntityDisplay(
    private val ref: Ref<out EntityInstanceEntry>,
    override val creator: EntityCreator,
    private val activityCreators: ActivityCreator,
    private val suppliers: List<Pair<PropertySupplier<*>, Int>>,
    private val spawnLocation: Location,
) : AudienceFilter(ref), TickableDisplay, ActivityEntityDisplay {
    private var activityManager: ActivityManager<SharedActivityContext>? = null
    private val entities = ConcurrentHashMap<UUID, DisplayEntity>()

    override fun filter(player: Player): Boolean {
        val npcLocation = activityManager?.location ?: return false
        val distance = npcLocation.distanceSqrt(player.location) ?: return false
        return distance <= entityShowRange * entityShowRange
    }

    override fun initialize() {
        super.initialize()
        val context = SharedActivityContext(ref, players)
        activityManager =
            ActivityManager(activityCreators.create(context, spawnLocation.toProperty()))
        activityManager?.initialize(context)
    }

    override fun onPlayerFilterAdded(player: Player) {
        super.onPlayerFilterAdded(player)
        val activityManager = activityManager ?: return
        entities.computeIfAbsent(player.uniqueId) {
            DisplayEntity(player, creator, activityManager, suppliers.toCollectors())
        }
    }

    override fun tick() {
        consideredPlayers.forEach { it.refresh() }

        // This is not an exact solution.
        // When the state is different between players, it might look weird.
        // But there is no real solution to this.
        // So we pick the first entity's state and use to try and keep the state consistent.
        val entityState = entities.values.firstOrNull()?.state ?: EntityState()
        activityManager?.tick(SharedActivityContext(ref, players, entityState))
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
        activityManager?.dispose(SharedActivityContext(ref, players))
        activityManager = null
    }

    override fun playerSeesEntity(playerId: UUID, entityId: Int): Boolean {
        return entities[playerId]?.contains(entityId) ?: false
    }

    override fun location(playerId: UUID): Location? = activityManager?.location?.toLocation()

    override fun canView(playerId: UUID): Boolean = canConsider(playerId)
}
