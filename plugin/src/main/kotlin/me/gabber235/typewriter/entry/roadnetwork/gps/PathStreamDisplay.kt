package me.gabber235.typewriter.entry.roadnetwork.gps

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.await
import lirand.api.extensions.server.server
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.entries.TickableDisplay
import me.gabber235.typewriter.entry.entries.roadNetworkMaxDistance
import me.gabber235.typewriter.entry.roadnetwork.content.toLocation
import me.gabber235.typewriter.entry.roadnetwork.content.toPathPosition
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import me.gabber235.typewriter.utils.distanceSqrt
import me.gabber235.typewriter.utils.firstWalkableLocationBelow
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.patheloper.api.pathing.configuration.PathingRuleSet
import org.patheloper.api.pathing.strategy.strategies.WalkablePathfinderStrategy
import org.patheloper.api.wrapper.PathPosition
import org.patheloper.mapping.PatheticMapper
import java.util.*

private val pathStreamRefreshTime by snippet(
    "path_stream.refresh_time",
    1700,
    "The time in milliseconds between a new stream being calculated.",
)

class PathStreamDisplay(
    private val ref: Ref<RoadNetworkEntry>,
    private val startLocation: (Player) -> Location = Player::getLocation,
    private val endLocation: (Player) -> Location,
) : AudienceDisplay(), TickableDisplay {
    private val displays = mutableMapOf<UUID, PlayerPathStreamDisplay>()
    override fun onPlayerAdd(player: Player) {
        displays[player.uniqueId] = PlayerPathStreamDisplay(ref, player, startLocation, endLocation)
    }

    override fun onPlayerRemove(player: Player) {
        displays.remove(player.uniqueId)?.dispose()
    }

    override fun tick() {
        displays.values.forEach { it.tick() }
    }
}

class MultiPathStreamDisplay(
    private val ref: Ref<RoadNetworkEntry>,
    private val startLocation: (Player) -> Location = Player::getLocation,
    private val endLocations: (Player) -> List<Location>,
) : AudienceDisplay(), TickableDisplay {
    private val displays = mutableMapOf<UUID, MutableList<StreamDisplay>>()

    override fun tick() {
        displays.forEach { (uuid, displays) ->
            val player = server.getPlayer(uuid) ?: return@forEach
            val endLocations = endLocations(player)
            while (displays.size < endLocations.size) {
                displays.add(StreamDisplay(ref, player, startLocation, endLocations[displays.size]))
            }
            while (displays.size > endLocations.size) {
                displays.removeLastOrNull()?.display?.dispose()
            }
            displays.forEachIndexed { index, stream ->
                stream.location = endLocations[index]
                stream.display.tick()
            }
        }
    }

    override fun onPlayerAdd(player: Player) {
        displays[player.uniqueId] = mutableListOf()
    }

    override fun onPlayerRemove(player: Player) {
        displays.remove(player.uniqueId)?.forEach { it.display.dispose() }
    }
}

private class StreamDisplay(
    ref: Ref<RoadNetworkEntry>,
    player: Player,
    startLocation: (Player) -> Location,
    var location: Location,
) {
    val display = PlayerPathStreamDisplay(ref, player, startLocation) { location }
}

private class PlayerPathStreamDisplay(
    ref: Ref<RoadNetworkEntry>,
    private val player: Player,
    private val startLocation: (Player) -> Location,
    private val endLocation: (Player) -> Location,
) {
    private var gps = PointToPointGPS(ref, { startLocation(player) }, { endLocation(player) })
    private var edges = emptyList<GPSEdge>()

    // The path for the last edge.
//    private var path = emptyList<Location>()
    private val lines = mutableListOf<PathLine>()
    private var lastRefresh = 0L

    private var job: Job? = null

    fun tick() {
        if (job?.isActive == false) {
            lastRefresh = System.currentTimeMillis()
            job = null
        }
        if (job == null && (System.currentTimeMillis() - lastRefresh) > pathStreamRefreshTime) {
            job = refreshPath()
        }

        displayPath()
    }

    private fun displayPath() {
        lines.retainAll { line ->
            val location = line.currentLocation ?: return@retainAll false
            player.spawnParticle(
                Particle.TOTEM,
                location.also { it.y += 0.5 },
                1,
                0.0,
                0.0,
                0.0,
                0.0,
            )

            line.next()
        }
    }

    private fun refreshPath() = DISPATCHERS_ASYNC.launch {
        val start = startLocation(player).firstWalkableLocationBelow
        val end = endLocation(player).firstWalkableLocationBelow

        // When the start and end location are the same, we don't need to find a path.
        if ((start.distanceSqrt(end) ?: Double.MAX_VALUE) < 1) {
            return@launch
        }

        edges = gps.findPath().getOrElse { emptyList() }
        coroutineScope {
            // We only need to calculate the paths that the player will be able to see
            val path = edges.filter {
                ((it.start.distanceSqrt(start) ?: Double.MAX_VALUE) < roadNetworkMaxDistance * roadNetworkMaxDistance
                        || (it.end.distanceSqrt(start)
                    ?: Double.MAX_VALUE) < roadNetworkMaxDistance * roadNetworkMaxDistance)
            }
                .map { edge ->
                    async {
                        findPath(edge.start, edge.end)
                    }
                }
                .awaitAll()
                .flatten()
                .map { it.mid().toLocation() }
            if (path.isEmpty()) return@coroutineScope
            lines.add(PathLine(path))
        }
    }

    private suspend fun findPath(
        start: Location,
        end: Location,
    ): Iterable<PathPosition> {
        val pathFinder = PatheticMapper.newPathfinder(
            PathingRuleSet.createAsyncRuleSet()
                .withLoadingChunks(true)
                .withAllowingDiagonal(true)
                .withAllowingFailFast(true)
        )
        val result = pathFinder.findPath(
            start.toPathPosition(),
            end.toPathPosition(),
            WalkablePathfinderStrategy()
        ).await()

        if (result.hasFailed()) return emptyList()
        return result.path
    }

    fun dispose() {
        job?.cancel()
    }
}

private data class PathLine(
    val path: List<Location>,
    var index: Int = 0,
) {
    val currentLocation: Location?
        get() = path.getOrNull(index)

    fun next(): Boolean {
        index++
        return index < path.size
    }
}