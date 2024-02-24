package me.gabber235.typewriter.entries.cinematic

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.*
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.packetevents.meta
import me.gabber235.typewriter.extensions.packetevents.spectateEntity
import me.gabber235.typewriter.extensions.packetevents.stopSpectatingEntity
import me.gabber235.typewriter.extensions.packetevents.toPacketLocation
import me.gabber235.typewriter.interaction.InterceptionBundle
import me.gabber235.typewriter.interaction.interceptPackets
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.*
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.*
import me.gabber235.typewriter.utils.ThreadType.SYNC
import me.tofaa.entitylib.EntityLib
import me.tofaa.entitylib.meta.display.TextDisplayMeta
import me.tofaa.entitylib.spigot.SpigotEntityLibAPI
import me.tofaa.entitylib.wrapper.WrapperEntity
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffect.INFINITE_DURATION
import org.bukkit.potion.PotionEffectType.INVISIBILITY
import java.util.*

@Entry("camera_cinematic", "Create a cinematic camera path", Colors.CYAN, "fa6-solid:video")
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
    @Segments(icon = "fa6-solid:video")
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
    private var previousSegment: CameraSegment? = null
    private lateinit var action: CameraAction

    private var originalState: PlayerState? = null
    private var interceptor: InterceptionBundle? = null

    override suspend fun setup() {
        action = if (player.isFloodgate) {
            TeleportCameraAction(player)
        } else {
            DisplayCameraAction(player)
        }
        super.setup()
    }

    override suspend fun tick(frame: Int) {
        super.tick(frame)

        val segment = (entry.segments activeSegmentAt frame)

        if (segment != previousSegment) {
            if (previousSegment == null && segment != null) {
                player.setup()
                action.startSegment(segment)
            } else if (segment != null) {
                action.switchSegment(segment)
            } else {
                action.stop()
                player.teardown()
            }

            previousSegment = segment
        }

        if (segment != null) {
            val baseFrame = frame - segment.startFrame
            action.tickSegment(baseFrame)
        }
    }

    private suspend fun Player.setup() {
        if (originalState == null) originalState = state(
            LOCATION,
            ALLOW_FLIGHT,
            FLYING,
            VISIBLE_PLAYERS,
            SHOWING_PLAYER,
            EffectStateProvider(INVISIBILITY)
        )

        SYNC.switchContext {
            allowFlight = true
            isFlying = true
            addPotionEffect(PotionEffect(INVISIBILITY, INFINITE_DURATION, 0, false, false))
            lirand.api.extensions.server.server.onlinePlayers.forEach {
                it.hidePlayer(plugin, this)
                this.hidePlayer(plugin, it)
            }

            // In creative mode, when the player opens the inventory while their inventory is fake cleared,
            // The actual inventory will be cleared.
            // To prevent this, we only fake clear the inventory when the player is not in creative mode.
            if (gameMode != GameMode.CREATIVE) {
                fakeClearInventory()
            }
        }

        interceptor = this.interceptPackets {
            !PacketType.Play.Client.CLICK_WINDOW
            !PacketType.Play.Client.CLICK_WINDOW_BUTTON
            !PacketType.Play.Client.USE_ITEM
            !PacketType.Play.Client.INTERACT_ENTITY
            PacketType.Play.Server.PLAYER_POSITION_AND_LOOK { event ->
                val packet = WrapperPlayServerPlayerPositionAndLook(event)
                packet.y += 500
            }
            PacketType.Play.Client.PLAYER_POSITION { event ->
                val packet = WrapperPlayClientPlayerPosition(event)
                packet.position = packet.position.withY(packet.position.y - 500)
            }
            PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION { event ->
                val packet = WrapperPlayClientPlayerPositionAndRotation(event)
                packet.position = packet.position.withY(packet.position.y - 500)
            }
        }
    }

    private suspend fun Player.teardown() {
        originalState?.let {
            SYNC.switchContext {
                restore(it)
                if (gameMode != GameMode.CREATIVE) {
                    restoreInventory()
                }
            }
        }
        originalState = null

        interceptor?.cancel()
    }

    override suspend fun teardown() {
        super.teardown()
        action.stop()
        player.teardown()
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
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
        teleport(location)
        allowFlight = true
        isFlying = true
    }
}

private data class PointSegment(
    override val startFrame: Int,
    override val endFrame: Int,
    val location: Location,
) : Segment

private interface CameraAction {
    suspend fun startSegment(segment: CameraSegment)
    suspend fun tickSegment(frame: Int)
    suspend fun switchSegment(newSegment: CameraSegment)
    suspend fun stop()
}

private class DisplayCameraAction(
    val player: Player,
) : CameraAction {
    private var entity = createEntity()
    private var path = emptyList<PointSegment>()

    companion object {
        private const val BASE_INTERPOLATION = 10
    }

    private fun createEntity(): WrapperEntity {
        return EntityLib.getApi<SpigotEntityLibAPI>().createEntity<WrapperEntity>(EntityTypes.TEXT_DISPLAY)
            .meta<TextDisplayMeta> {
                positionRotationInterpolationDuration = BASE_INTERPOLATION
            }
    }

    private fun setupPath(segment: CameraSegment) {
        path = segment.path.transform(segment.duration - BASE_INTERPOLATION) {
            it.clone().apply {
                y += player.eyeHeight
            }
        }
    }

    override suspend fun startSegment(segment: CameraSegment) {
        setupPath(segment)

        SYNC.switchContext {
            player.teleport(path.first().location)
        }

        entity.spawn(path.first().location.toPacketLocation())
        entity.addViewer(player.uniqueId)
        player.spectateEntity(entity)
    }

    override suspend fun tickSegment(frame: Int) {
        val location = path.interpolate(frame)
        entity.rotateHead(location.yaw, location.pitch)
        entity.teleport(location.toPacketLocation())
        player.teleportIfNeeded(frame, location)
    }

    override suspend fun switchSegment(newSegment: CameraSegment) {
        val oldWorld = path.first().location.world.uid
        val newWorld = newSegment.path.first().location.world.uid

        setupPath(newSegment)
        if (oldWorld == newWorld) {
            switchSeamless()
        } else {
            switchWithStop()
        }
    }

    private suspend fun switchSeamless() {
        val newEntity = createEntity()
        newEntity.spawn(path.first().location.toPacketLocation())
        newEntity.addViewer(player.uniqueId)

        SYNC.switchContext {
            player.teleport(path.first().location)
            player.spectateEntity(newEntity)
        }

        entity.despawn()
        entity.remove()
        entity = newEntity
    }

    private suspend fun switchWithStop() {
        player.stopSpectatingEntity()
        entity.despawn()
        entity.addViewer(player.uniqueId)
        SYNC.switchContext {
            player.teleport(path.first().location)
            entity.spawn(path.first().location.toPacketLocation())
            player.spectateEntity(entity)
        }
    }

    override suspend fun stop() {
        player.stopSpectatingEntity()
        entity.despawn()
        entity.remove()
    }
}

private class TeleportCameraAction(
    private val player: Player,
) : CameraAction {
    private var path = emptyList<PointSegment>()

    override suspend fun startSegment(segment: CameraSegment) {
        path = segment.path.transform(segment.duration, Location::clone)
    }

    override suspend fun tickSegment(frame: Int) {
        val location = path.interpolate(frame)
        SYNC.switchContext {
            player.teleport(location)
            player.allowFlight = true
            player.isFlying = true
        }
    }

    override suspend fun switchSegment(newSegment: CameraSegment) {
    }

    override suspend fun stop() {
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
    if (isEmpty()) {
        throw IllegalArgumentException("The path points cannot be empty.")
    }

    if (size == 1) {
        val pathPoint = first()
        val location = pathPoint.location.run(locationTransformer)
        return listOf(PointSegment(0, totalDuration, location))
    }


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

