package me.gabber235.typewriter.entries.cinematic

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.*
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.packetevents.meta
import me.gabber235.typewriter.extensions.packetevents.spectateEntity
import me.gabber235.typewriter.extensions.packetevents.stopSpectatingEntity
import me.gabber235.typewriter.extensions.packetevents.toPacketLocation
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.*
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.*
import me.gabber235.typewriter.utils.ThreadType.SYNC
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.meta.display.TextDisplayMeta
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffect.INFINITE_DURATION
import org.bukkit.potion.PotionEffectType.INVISIBILITY
import java.util.*
import kotlin.math.min

@Entry("camera_cinematic", "Create a cinematic camera path", Colors.CYAN, Icons.VIDEO)
/**
 * The `Camera Cinematic` entry is used to create a cinematic camera path.
 *
 * :::tip
 * When starting a camera, Minecraft needs `10` frames to load camera's.
 * It is suggested to use a [Blinding Cinematic Entry](./blinding_cinematic) and wait for `10`-`20` frames.
 * Before the first segment to get the smoothest cinematic.
 * :::
 *
 * ## How could this be used?
 * When you want to direct the player's attention to a specific object/location.
 * Or when you want to show off a build.
 */
class CameraCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(icon = Icons.VIDEO)
    @InnerMin(Min(10))
    val segments: List<CameraSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return CameraCinematicAction(
            player,
            this,
        )
    }

    override fun createSimulated(player: Player): CinematicAction {
        return SimulatedCameraCinematicAction(
            player,
            this,
        )
    }

}

data class CameraSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    val path: List<PathPoint> = emptyList(),
) : Segment

data class PathPoint(
    @WithRotation
    val location: Location = Location(null, 0.0, 0.0, 0.0),
    @Help("The duration of the path point in frames.")
    /**
     * The duration of the path point in frames.
     * If not set,
     * the duration will be calculated based on the total duration and the number of path points
     * that don't have a duration.
     */
    val duration: Optional<Int> = Optional.empty(),
)

class CameraCinematicAction(
    private val player: Player,
    private val entry: CameraCinematicEntry,
) : CinematicAction {
    private var segments = emptyList<CameraSegmentAction>()

    private var currentSegmentAction: CameraSegmentAction? = null
    private var originalState: PlayerState? = null

    override suspend fun setup() {
        super.setup()

        originalState = player.state(
            LOCATION,
            ALLOW_FLIGHT,
            FLYING,
            VISIBLE_PLAYERS,
            SHOWING_PLAYER,
            EffectStateProvider(INVISIBILITY)
        )

        SYNC.switchContext {
            player.allowFlight = true
            player.isFlying = true
            player.addPotionEffect(PotionEffect(INVISIBILITY, INFINITE_DURATION, 0, false, false))
            server.onlinePlayers.forEach {
                it.hidePlayer(plugin, player)
                player.hidePlayer(plugin, it)
            }

            // Move the player before to the first location. This will spawn the boats in the correct world.
            // And gives the client time to load the chunks.
            val firstLocation = entry.segments.firstOrNull()?.path?.firstOrNull()?.location
            firstLocation?.let {
                player.teleport(it)
            }
        }


        segments = entry.segments.mapNotNull { segment ->
            if (segment.path.isEmpty()) {
                logger.warning("Camera segment has no path in ${entry.id}, skipping.")
                return@mapNotNull null
            }
            if (player.isFloodgate) {
                TeleportCameraSegmentAction(player, segment)
            } else {
                BlockDisplayCameraSegmentAction(player, segment)
            }
        }
        segments.forEach { it.setup() }
    }

    override suspend fun tick(frame: Int) {
        super.tick(frame)

        if (currentSegmentAction?.isActiveAt(frame) == false) {
            currentSegmentAction?.stop()
            currentSegmentAction = null
        }

        if (currentSegmentAction == null) {
            currentSegmentAction = findSegmentAction(frame)
            currentSegmentAction?.start()
        }

        currentSegmentAction?.tick(frame)
    }

    private fun findSegmentAction(frame: Int): CameraSegmentAction? {
        return segments.firstOrNull { it isActiveAt frame }
    }


    override suspend fun teardown() {
        super.teardown()

        currentSegmentAction?.stop()
        currentSegmentAction = null

        segments.forEach { it.teardown() }
        segments = emptyList()

        originalState?.let {
            SYNC.switchContext {
                player.restore(it)
            }
        }
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}


private interface CameraSegmentAction {
    val segment: CameraSegment
    fun setup()
    suspend fun start()
    suspend fun tick(frame: Int)
    fun stop()
    fun teardown()
    infix fun isActiveAt(frame: Int): Boolean = segment isActiveAt frame
}

// The max distance the entity can be from the player before it gets teleported.
private const val MAX_DISTANCE_SQUARED = 25 * 25

/**
 * Teleports the player to the given location if needed.
 * To render chunks correctly, we need to teleport the player to the entity.
 * Though to prevent lag, we only do this every 10 frames or when the player is too far away.
 *
 * @param frame The current frame.
 * @param location The location to teleport to.
 */
private suspend inline fun Player.teleportIfNeeded(
    frame: Int,
    location: Location,
) {
    if (frame % 10 == 0 || location.distanceSquared(location) > MAX_DISTANCE_SQUARED) SYNC.switchContext {
        teleport(location.highUpLocation)
        allowFlight = true
        isFlying = true
    }
}

private data class PointSegment(
    override val startFrame: Int,
    override val endFrame: Int,
    val location: Location,
) : Segment


private class BlockDisplayCameraSegmentAction(
    private val player: Player,
    override val segment: CameraSegment,
) : CameraSegmentAction {
    /**
     * We need to have the last segment interpolation such that the entity has time at the end to finish the path.
     * Only since the last segment never moves, since the cinematic ends at the last frame.
     * We need to get the second last segment interpolation.
     */
    private val secondToLastSegmentInterpolation by lazy {
        if (segment.path.size < 2) return@lazy BASE_INTERPOLATION
        val point = segment.path[segment.path.size - 2]
        val duration = point.duration.orElse(BASE_INTERPOLATION)
        min(duration, BASE_INTERPOLATION)
    }
    private val path = segment.path.transform(segment.duration - secondToLastSegmentInterpolation) {
        it.clone().apply {
            y += player.eyeHeight
        }
    }

    private val entity = EntityLib.createEntity(UUID.randomUUID(), EntityTypes.TEXT_DISPLAY)

    companion object {
        private const val BASE_INTERPOLATION = 10
    }

    override fun setup() {
        entity.addViewer(player.uniqueId)
        entity.spawn(path.first().location.toPacketLocation())
    }

    override suspend fun start() {
        SYNC.switchContext {
            // Teleport the player to the first location.
            // We need to use the unmodified location here.
            // Otherwise, the player will be shifted when spectating.
            player.teleport(segment.path.first().location)
            player.spectateEntity(entity)
        }
    }

    override suspend fun tick(frame: Int) {
        val baseFrame = frame - segment.startFrame
        val segment = (path activeSegmentAt baseFrame) ?: path.last()
        val location = path.interpolate(baseFrame)

        // If the segment duration is less than the base interpolation,
        // we need to lower the interpolation to allow the entity to move quicker.
        val interpolation = min(segment.duration, BASE_INTERPOLATION)

        entity.meta<TextDisplayMeta> {
            if (positionRotationInterpolationDuration == interpolation) return@meta
            positionRotationInterpolationDuration = interpolation
        }

        entity.rotateHead(location.yaw, location.pitch)
        entity.teleport(location.toPacketLocation())
        player.teleportIfNeeded(frame, location)
    }

    override fun stop() {
        player.stopSpectatingEntity()
    }

    override fun teardown() {
        entity.remove()
    }
}

private class TeleportCameraSegmentAction(
    private val player: Player,
    override val segment: CameraSegment,
) : CameraSegmentAction {
    private val path = segment.path.transform(segment.duration, Location::clone)
    private val firstLocation = path.first().location
    override fun setup() {
    }

    override suspend fun start() {
        SYNC.switchContext {
            player.teleport(firstLocation)
        }
    }

    override suspend fun tick(frame: Int) {
        val baseFrame = frame - segment.startFrame
        val location = path.interpolate(baseFrame)
        SYNC.switchContext {
            player.teleport(location)
            player.allowFlight = true
            player.isFlying = true
        }
    }

    override fun stop() {
    }

    override fun teardown() {
    }
}

class SimulatedCameraCinematicAction(
    private val player: Player,
    private val entry: CameraCinematicEntry,
) : CinematicAction {

    private val paths = entry.segments.associateWith { segment ->
        segment.path.transform(segment.duration) {
            it.clone().apply {
                y += player.eyeHeight
            }
        }
    }

    override suspend fun tick(frame: Int) {
        super.tick(frame)

        val segment = (entry.segments activeSegmentAt frame) ?: return
        val path = paths[segment] ?: return
        val location = path.interpolate(frame - segment.startFrame)

        // Display a particle at the location
        player.spawnParticle(Particle.SCRAPE, location, 1)
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}

private fun List<PathPoint>.transform(
    totalDuration: Int,
    locationTransformer: (Location) -> Location
): List<PointSegment> {
    val allocatedDuration = sumOf { it.duration.orElse(0) }
    if (allocatedDuration > totalDuration) {
        throw IllegalArgumentException("The total duration of the path points is greater than the total duration of the cinematic.")
    }

    val remainingDuration = totalDuration - allocatedDuration

    // The last segment should never have a duration. As the last segment will be reached when the cinematic ends.
    val leftSegments = take(size - 1).count { it.duration.isEmpty }

    if (leftSegments == 0) {
        if (remainingDuration > 0) {
            logger.warning("The sum duration of the path points is less than the total duration of the cinematic. The remaining duration will be still frames.")
        }

        var currentFrame = 0
        return map {
            val endFrame = currentFrame + it.duration.orElse(0)
            val segment = PointSegment(currentFrame, endFrame, it.location.run(locationTransformer))
            currentFrame = endFrame
            segment
        }
    }

    val durationPerSegment = remainingDuration / leftSegments
    var leftOverDuration = remainingDuration % leftSegments

    var currentFrame = 0

    return map { pathPoint ->
        val duration = pathPoint.duration.orElseGet {
            if (leftOverDuration > 0) {
                leftOverDuration--
                durationPerSegment + 1
            } else {
                durationPerSegment
            }
        }
        val endFrame = currentFrame + duration
        val segment = PointSegment(currentFrame, endFrame, pathPoint.location.run(locationTransformer))
        currentFrame = endFrame
        segment
    }
}

/**
 * Use catmull-rom interpolation to get a point between a list of points.
 */
private fun List<PointSegment>.interpolate(frame: Int): Location {
    val index = indexOfFirst { it isActiveAt frame }
    if (index == -1) {
        return last().location
    }

    val segment = this[index]
    val totalFrames = segment.endFrame - segment.startFrame
    val currentFrame = frame - segment.startFrame
    val percentage = currentFrame.toDouble() / totalFrames

    val currentLocation = segment.location
    val previousLocation = getOrNull(index - 1)?.location ?: currentLocation
    val nextLocation = getOrNull(index + 1)?.location ?: currentLocation
    val nextNextLocation = getOrNull(index + 2)?.location ?: nextLocation

    return interpolatePoints(previousLocation, currentLocation, nextLocation, nextNextLocation, percentage)
}

/**
 * Use catmull-rom interpolation to get a point between four points.
 */
fun interpolatePoints(
    previousPoint: Location,
    currentPoint: Location,
    nextPoint: Location,
    nextNextPoint: Location,
    percentage: Double,
): Location {
    val x = interpolatePoints(
        previousPoint.x,
        currentPoint.x,
        nextPoint.x,
        nextNextPoint.x,
        percentage,
    )
    val y = interpolatePoints(
        previousPoint.y,
        currentPoint.y,
        nextPoint.y,
        nextNextPoint.y,
        percentage,
    )
    val z = interpolatePoints(
        previousPoint.z,
        currentPoint.z,
        nextPoint.z,
        nextNextPoint.z,
        percentage,
    )

    val previousYaw = previousPoint.yaw.toDouble()
    val currentYaw = correctYaw(previousYaw, currentPoint.yaw.toDouble())
    val nextYaw = correctYaw(currentYaw, nextPoint.yaw.toDouble())
    val nextNextYaw = correctYaw(nextYaw, nextNextPoint.yaw.toDouble())
    val yaw = interpolatePoints(
        previousYaw,
        currentYaw,
        nextYaw,
        nextNextYaw,
        percentage,
    )

    val pitch = interpolatePoints(
        previousPoint.pitch.toDouble(),
        currentPoint.pitch.toDouble(),
        nextPoint.pitch.toDouble(),
        nextNextPoint.pitch.toDouble(),
        percentage,
    )

    return Location(
        currentPoint.world,
        x,
        y,
        z,
        yaw.toFloat(),
        pitch.toFloat(),
    )
}

/**
 * Use catmull-rom interpolation to get a point between four points.
 */
fun interpolatePoints(
    previousPoint: Double,
    currentPoint: Double,
    nextPoint: Double,
    nextNextPoint: Double,
    percentage: Double,
): Double {
    val square = percentage * percentage
    val cube = square * percentage

    return 0.5 * (
            (2 * currentPoint) +
                    (-previousPoint + nextPoint) * percentage +
                    (2 * previousPoint - 5 * currentPoint + 4 * nextPoint - nextNextPoint) * square +
                    (-previousPoint + 3 * currentPoint - 3 * nextPoint + nextNextPoint) * cube
            )
}

/**
 * Correct the yaw rotation so that it correctly interpolates between -180 and 180.
 */
fun correctYaw(currentYaw: Double, nextYaw: Double): Double {
    val difference = nextYaw - currentYaw
    return if (difference > 180) {
        nextYaw - 360
    } else if (difference < -180) {
        nextYaw + 360
    } else {
        nextYaw
    }
}

