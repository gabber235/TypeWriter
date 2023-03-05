package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.WithRotation
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.placeholderapi.*
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

@Entry("camera_cinematic", "Create a cinematic camera path", Colors.CYAN, Icons.VIDEO)
data class CameraCinematicEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val segments: List<CameraSegment> = emptyList(),
) : CinematicEntry<CameraSegment> {
	override fun create(player: Player): CinematicAction {
		return CameraCinematicAction(
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

private data class PlayerState(
	val location: Location,
)

private val Player.state: PlayerState
	get() = PlayerState(
		location,
	)

private fun Player.restore(state: PlayerState) {
	teleport(state.location)
}

class CameraCinematicAction(
	private val player: Player,
	private val entry: CameraCinematicEntry,
) : CinematicAction {
	private var segments = emptyList<CameraSegmentAction>()

	private var currentSegmentAction: CameraSegmentAction? = null
	private var originalState: PlayerState? = null

	override fun setup() {
		super.setup()

		segments = entry.segments.map { CameraSegmentAction(player, it) }
		segments.forEach { it.setup() }

		originalState = player.state
	}

	override fun tick(frame: Int) {
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


	override fun teardown() {
		super.teardown()

		currentSegmentAction?.stop()
		currentSegmentAction = null

		segments.forEach { it.teardown() }
		segments = emptyList()

		originalState?.let { player.restore(it) }
	}

	override fun canFinish(frame: Int): Boolean = entry canFinishAt frame
}

private class CameraSegmentAction(
	private val player: Player,
	private val segment: CameraSegment,
) {
	private val entityId = (Math.random() * 1000000).toInt()
	private val firstPoint = segment.path.first()

	fun setup() {
		player.spawnClientSideEntity(entityId, entityType = EntityType.BOAT, location = firstPoint.location)
	}

	fun start() {
		player.teleport(firstPoint.location)
		player.spectateEntity(entityId)
	}

	fun tick(frame: Int) {
		val percentage = percentage(frame)
		val location = segment.path.interpolate(percentage)
//		player.teleport(location)
		player.teleportEntity(entityId, location)
	}

	fun stop() {
		player.spectateEntity(null)
	}

	fun teardown() {
		player.despawnClientSideEntity(entityId)
	}

	private fun percentage(frame: Int): Double {
		val totalFrames = segment.endFrame - segment.startFrame
		val currentFrame = frame - segment.startFrame
		return currentFrame.toDouble() / totalFrames
	}

	infix fun isActiveAt(frame: Int): Boolean = segment isActiveAt frame
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

	val yaw = interpolatePoints(
		previousPoint.yaw.toDouble(),
		currentPoint.yaw.toDouble(),
		nextPoint.yaw.toDouble(),
		nextNextPoint.yaw.toDouble(),
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