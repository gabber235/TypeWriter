package me.gabber235.typewriter.entries.cinematic

import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.withContext
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.InnerMin
import me.gabber235.typewriter.adapters.modifiers.Min
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.adapters.modifiers.WithRotation
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.protocollib.BoatType
import me.gabber235.typewriter.extensions.protocollib.ClientEntity
import me.gabber235.typewriter.extensions.protocollib.spectateEntity
import me.gabber235.typewriter.extensions.protocollib.stopSpectatingEntity
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.*
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.*
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffect.INFINITE_DURATION
import org.bukkit.potion.PotionEffectType.INVISIBILITY

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
    val location: Location,
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

        segments = entry.segments.mapNotNull { segment ->
            if (segment.path.isEmpty()) {
                logger.warning("Camera segment has no path in ${entry.id}, skipping.")
                return@mapNotNull null
            }
            if (player.isFloodgate) {
                TeleportCameraSegmentAction(player, segment)
            } else {
                val hasSegmentBefore = entry.segments.any { it isActiveAt (segment.startFrame - 1) }
                BoatCameraSegmentAction(player, segment, hasSegmentBefore)
            }
        }
        segments.forEach { it.setup() }

        originalState = player.state(
            LOCATION,
            ALLOW_FLIGHT,
            FLYING,
            VISIBLE_PLAYERS,
            SHOWING_PLAYER,
            EffectStateProvider(INVISIBILITY)
        )

        withContext(plugin.minecraftDispatcher) {
            player.allowFlight = true
            player.isFlying = true
            player.addPotionEffect(PotionEffect(INVISIBILITY, INFINITE_DURATION, 0, false, false))
            server.onlinePlayers.forEach {
                it.hidePlayer(plugin, player)
                player.hidePlayer(plugin, it)
            }
        }
    }

    override suspend fun tick(frame: Int) {
        super.tick(frame)

        segments.filter { it.canPrepare(frame) }.forEach {
            it.prepare()
        }


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
            withContext(plugin.minecraftDispatcher) {
                player.restore(it)
            }
        }
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}


private interface CameraSegmentAction {
    val segment: CameraSegment
    fun setup()
    suspend fun prepare()
    fun canPrepare(frame: Int): Boolean
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
    if (frame % 10 == 0 || location.distanceSquared(location) > MAX_DISTANCE_SQUARED) withContext(
        plugin.minecraftDispatcher
    ) {
        teleport(location.clone().apply {
            y += 10
        })
        allowFlight = true
        isFlying = true
    }
}


private class BoatCameraSegmentAction(
    private val player: Player,
    override val segment: CameraSegment,
    private val hasSegmentBefore: Boolean,
) : CameraSegmentAction {
    companion object {
        // As a boat is interpolated. We need the remove the last few frames to make sure the animation fully finishes.
        private const val TRAILING_FRAMES = 10

        // The amount of ticks the boat gets to prepare before the player is teleported to it.
        private const val PREPARE_TICKS = 10

        private const val BOAT_HEIGHT = 0.5625
    }

    private val path = segment.path.map {
        it.copy(
            location = it.location.clone().apply { y += player.eyeHeight - BOAT_HEIGHT }
        )
    }
    private val firstLocation = path.first().location

    private val farAwayLocation = firstLocation.highUpLocation

    private val startLocation
        get() = if (segment.startFrame > PREPARE_TICKS && hasSegmentBefore) farAwayLocation else firstLocation

    private val entity = ClientEntity(startLocation, EntityType.BOAT).also {
        it.boatType = BoatType.JUNGLE
    }

    override fun setup() {
        entity.addViewer(player)
    }

    override suspend fun prepare() {
        entity.move(firstLocation)

        // If the player is up in the sky, we know that they can't see land.
        // As such, we can teleport them to the x/z of the first location.
        if (player.isHighUp) {
            val location = firstLocation.highUpLocation
            withContext(plugin.minecraftDispatcher) {
                player.teleport(location)
            }
        }
    }

    override fun canPrepare(frame: Int): Boolean {
        return segment.startFrame - PREPARE_TICKS == frame
    }

    override suspend fun start() {
        withContext(plugin.minecraftDispatcher) {
            // Teleport the player to the first location.
            // We need to use the unmodified location here.
            // Otherwise, the player will be shifted when spectating.
            val firstNormalLocation = segment.path.first().location
            player.teleport(firstNormalLocation)
            player.spectateEntity(entity)
        }
    }

    override suspend fun tick(frame: Int) {
        val percentage = percentage(frame)
        val location = path.interpolate(percentage)

        entity.move(location)
        player.teleportIfNeeded(frame, location)
    }

    private fun percentage(frame: Int): Double {
        val totalFrames = segment.endFrame - segment.startFrame - TRAILING_FRAMES
        val currentFrame = frame - segment.startFrame
        return (currentFrame.toDouble() / totalFrames).coerceIn(0.0, 1.0)
    }

    override fun stop() {
        player.stopSpectatingEntity()
        entity.move(farAwayLocation)
    }

    override fun teardown() {
        entity.removeViewer(player)
    }
}

private class TeleportCameraSegmentAction(
    private val player: Player,
    override val segment: CameraSegment,
) : CameraSegmentAction {
    private val firstLocation = segment.path.first().location

    override fun setup() {
    }

    override suspend fun prepare() {
    }

    override fun canPrepare(frame: Int): Boolean {
        return segment.startFrame - 10 == frame
    }

    override suspend fun start() {
        withContext(plugin.minecraftDispatcher) {
            player.teleport(firstLocation)
        }
    }

    override suspend fun tick(frame: Int) {
        val percentage = percentage(frame)
        val location = segment.path.interpolate(percentage)
        withContext(plugin.minecraftDispatcher) {
            player.teleport(location)
            player.allowFlight = true
            player.isFlying = true
        }
    }


    private fun percentage(frame: Int): Double {
        val totalFrames = segment.endFrame - segment.startFrame
        val currentFrame = frame - segment.startFrame
        return (currentFrame.toDouble() / totalFrames).coerceIn(0.0, 1.0)
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

    override suspend fun tick(frame: Int) {
        super.tick(frame)

        val segment = (entry.segments activeSegmentAt frame) ?: return
        val percentage = segment.percentageAt(frame)
        val location = segment.path.interpolate(percentage)

        // Display a particle at the location
        player.spawnParticle(Particle.SCRAPE, location, 1)
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}

/**
 * Use catmull-rom interpolation to get a point between a list of points.
 */
fun List<PathPoint>.interpolate(percentage: Double): Location {
    val currentPart = percentage * (size - 1)
    val index = currentPart.toInt()
    val subPercentage = currentPart - index

    val previousPoint = getOrNull(index - 1)?.location ?: this[index].location
    val currentPoint = this[index].location
    val nextPoint = getOrNull(index + 1)?.location ?: currentPoint
    val nextNextPoint = getOrNull(index + 2)?.location ?: nextPoint

    return interpolatePoints(previousPoint, currentPoint, nextPoint, nextNextPoint, subPercentage)
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

