package com.typewritermc.engine.paper.entry.entity

import lirand.api.extensions.server.server
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.utils.point.Position
import com.typewritermc.engine.paper.entry.entries.AudienceFilter
import com.typewritermc.engine.paper.entry.entries.EntityInstanceEntry
import com.typewritermc.engine.paper.entry.entries.PropertySupplier
import com.typewritermc.engine.paper.entry.entries.TickableDisplay
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class IndividualActivityEntityDisplay(
    private val ref: Ref<out EntityInstanceEntry>,
    override val creator: EntityCreator,
    private val activityCreator: ActivityCreator,
    private val suppliers: List<Pair<PropertySupplier<*>, Int>>,
    private val spawnPosition: Position,
) : AudienceFilter(ref), TickableDisplay, ActivityEntityDisplay {
    private val activityManagers = ConcurrentHashMap<UUID, ActivityManager<in IndividualActivityContext>>()
    private val entities = ConcurrentHashMap<UUID, DisplayEntity>()

    override fun filter(player: Player): Boolean {
        val activityManager = activityManagers[player.uniqueId] ?: return false
        val npcPosition = activityManager.position
        val distance = npcPosition.distanceSqrt(player.location) ?: return false
        return distance <= entityShowRange * entityShowRange
    }

    override fun onPlayerAdd(player: Player) {
        activityManagers.computeIfAbsent(player.uniqueId) {
            val context = IndividualActivityContext(ref, player)
            val activity = activityCreator.create(context, spawnPosition.toProperty())
            val activityManager = ActivityManager(activity)
            activityManager.initialize(context)
            activityManager
        }
        super.onPlayerAdd(player)
    }

    override fun onPlayerFilterAdded(player: Player) {
        super.onPlayerFilterAdded(player)
        val activityManager = activityManagers[player.uniqueId] ?: return
        entities.computeIfAbsent(player.uniqueId) {
            DisplayEntity(player, creator, activityManager, suppliers.toCollectors())
        }
    }

    override fun tick() {
        consideredPlayers.forEach { it.refresh() }

        activityManagers.forEach { (pid, manager) ->
            val player = server.getPlayer(pid) ?: return@forEach
            val isViewing = pid in this
            val entityState = entities[pid]?.state ?: EntityState()
            manager.tick(IndividualActivityContext(ref, player, isViewing, entityState))
        }
        entities.values.forEach { it.tick() }
    }

    override fun onPlayerFilterRemoved(player: Player) {
        super.onPlayerFilterRemoved(player)
        entities.remove(player.uniqueId)?.dispose()
    }

    override fun onPlayerRemove(player: Player) {
        super.onPlayerRemove(player)
        activityManagers.remove(player.uniqueId)?.dispose(IndividualActivityContext(ref, player))
    }

    override fun dispose() {
        super.dispose()
        entities.values.forEach { it.dispose() }
        entities.clear()
        activityManagers.entries.forEach { (playerId, activityManager) ->
            activityManager.dispose(IndividualActivityContext(ref, server.getPlayer(playerId) ?: return@forEach))
        }
        activityManagers.clear()
    }

    override fun playerSeesEntity(playerId: UUID, entityId: Int): Boolean {
        return entities[playerId]?.contains(entityId) ?: false
    }

    override fun position(playerId: UUID): Position? = activityManagers[playerId]?.position
    override fun canView(playerId: UUID): Boolean = canConsider(playerId)

    override fun entityId(playerId: UUID): Int {
        return entities[playerId]?.entityId ?: 0
    }
}