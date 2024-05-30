package me.gabber235.typewriter.utils

import org.bukkit.Location


/**
 * Use catmull-rom interpolation to get a point between a list of points.
 */
fun List<Location>.interpolate(percentage: Double): Location {
    val currentPart = percentage * (size - 1)
    val index = currentPart.toInt()
    val subPercentage = currentPart - index

    val previousPoint = getOrNull(index - 1) ?: this[index]
    val currentPoint = this[index]
    val nextPoint = getOrNull(index + 1) ?: currentPoint
    val nextNextPoint = getOrNull(index + 2) ?: nextPoint

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

