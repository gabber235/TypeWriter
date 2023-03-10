package me.gabber235.typewriter.entries.cinematic

import com.github.shynixn.mccoroutine.launch
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.adapters.modifiers.WithRotation
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.extensions.protocollib.*
import me.gabber235.typewriter.utils.*
import me.gabber235.typewriter.utils.GenericPlayerStateProvider.LOCATION
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.*

@Entry("camera_cinematic", "Create a cinematic camera path", Colors.CYAN, Icons.VIDEO)
data class CameraCinematicEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	@Segments(icon = Icons.STACKPATH)
	val segments: List<CameraSegment> = emptyList(),
) : CinematicEntry {
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
	val zoom: Optional<Double> = Optional.empty(),
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

	override fun setup() {
		super.setup()

		segments = entry.segments.map { CameraSegmentAction(player, it) }
		segments.forEach { it.setup() }

		originalState = player.state(LOCATION)
	}

	override fun tick(frame: Int) {
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


	override fun teardown() {
		super.teardown()

		currentSegmentAction?.stop()
		currentSegmentAction = null

		segments.forEach { it.teardown() }
		segments = emptyList()

		originalState?.let {
			plugin.launch {
				player.restore(it)
			}
		}
	}

	override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}

private class CameraSegmentAction(
	private val player: Player,
	private val segment: CameraSegment,
) {
	private val attributeModifier = AttributeModifier(
		UUID.randomUUID(),
		"camera_zoom",
		segment.zoom.orElse(1.0),
		AttributeModifier.Operation.MULTIPLY_SCALAR_1,
	)

	private val farAwayLocation = Location(player.world, 0.0, 500.0, 0.0)
	private val firstLocation = segment.path.first().location
	private val initializationLocation: Location
		get() = if (segment.startFrame > 10) farAwayLocation else firstLocation
	private val entity = ClientEntity(initializationLocation, EntityType.BOAT)

	fun setup() {
		entity.addViewer(player)
	}

	fun prepare() {
		entity.move(firstLocation)
	}

	fun canPrepare(frame: Int): Boolean {
		return segment.startFrame - 10 == frame
	}

	fun start() {
		plugin.launch {
			player.teleport(firstLocation)
			player.spectateEntity(entity)
			player.getAttribute(Attribute.GENERIC_FLYING_SPEED)?.addModifier(attributeModifier)
			println(
				"Setting speed to ${player.getAttribute(Attribute.GENERIC_FLYING_SPEED)?.value}, modifiers: ${
					player.getAttribute(
						Attribute.GENERIC_FLYING_SPEED
					)?.modifiers
				}"
			)
		}
	}

	fun tick(frame: Int) {
		val percentage = percentage(frame)
		val location = segment.path.interpolate(percentage)
		entity.move(location)
	}

	fun stop() {
		player.stopSpectatingEntity()
		entity.removeViewer(player)
		player.getAttribute(Attribute.GENERIC_FLYING_SPEED)?.removeModifier(attributeModifier)
	}

	fun teardown() {
		entity.removeViewer(player)
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

