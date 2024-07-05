package me.gabber235.typewriter.entry.roadnetwork.gps

import com.extollit.gaming.ai.path.HydrazinePathFinder
import com.extollit.linalg.immutable.Vec3d
import com.github.retrooper.packetevents.protocol.particle.Particle
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes
import com.github.retrooper.packetevents.util.Vector3f
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import lirand.api.extensions.server.server
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entity.toProperty
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.entries.TickableDisplay
import me.gabber235.typewriter.entry.entries.roadNetworkMaxDistance
import me.gabber235.typewriter.entry.roadnetwork.pathfinding.PFEmptyEntity
import me.gabber235.typewriter.entry.roadnetwork.pathfinding.PFInstanceSpace
import me.gabber235.typewriter.extensions.packetevents.sendPacketTo
import me.gabber235.typewriter.extensions.packetevents.toVector3d
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.ThreadType.DISPATCHERS_ASYNC
import me.gabber235.typewriter.utils.distanceSqrt
import me.gabber235.typewriter.utils.firstWalkableLocationBelow
import org.bukkit.Location
import org.bukkit.entity.Player
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
            WrapperPlayServerParticle(
                Particle(ParticleTypes.TOTEM_OF_UNDYING),
                true,
                location.also { it.y += 0.5 }.toVector3d(),
                Vector3f(0.3f, 0.0f, 0.3f),
                0f,
                1
            ) sendPacketTo player

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
                .map { it.toCenterLocation() }
            if (path.isEmpty()) return@coroutineScope
            lines.add(PathLine(path))
        }
    }

    private fun findPath(
        start: Location,
        end: Location,
    ): Iterable<Location> {
        val entity = PFEmptyEntity(start.toProperty(), searchRange = roadNetworkMaxDistance.toFloat())
        val instance = PFInstanceSpace(start.world)
        val pathfinder = HydrazinePathFinder(entity, instance)
        val path = pathfinder.computePathTo(Vec3d(end.x, end.y, end.z)) ?: return emptyList()
        return path.map {
            val coordinate = it.coordinates()
            Location(start.world, coordinate.x.toDouble(), coordinate.y.toDouble(), coordinate.z.toDouble())
        }
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