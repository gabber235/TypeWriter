package com.typewritermc.core.utils.point

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Coordinate(
    override val x: Double = 0.0,
    override val y: Double = 0.0,
    override val z: Double = 0.0,
    override val yaw: Float = 0f,
    override val pitch: Float = 0f,
) : com.typewritermc.core.utils.point.Point, com.typewritermc.core.utils.point.Rotatable {

    override fun withX(x: Double): com.typewritermc.core.utils.point.Coordinate = copy(x = x)

    override fun withY(y: Double): com.typewritermc.core.utils.point.Coordinate = copy(y = y)

    override fun withZ(z: Double): com.typewritermc.core.utils.point.Coordinate = copy(z = z)

    override fun withYaw(yaw: Float): com.typewritermc.core.utils.point.Coordinate = copy(yaw = yaw)

    override fun withPitch(pitch: Float): com.typewritermc.core.utils.point.Coordinate = copy(pitch = pitch)

    override fun rotateYaw(angle: Float): com.typewritermc.core.utils.point.Coordinate = copy(yaw = this.yaw + angle)

    override fun rotatePitch(angle: Float): com.typewritermc.core.utils.point.Coordinate = copy(pitch = this.pitch + angle)

    override fun rotate(yaw: Float, pitch: Float): com.typewritermc.core.utils.point.Coordinate = copy(yaw = this.yaw + yaw, pitch = this.pitch + pitch)

    override fun add(x: Double, y: Double, z: Double): com.typewritermc.core.utils.point.Coordinate {
        return copy(x = this.x + x, y = this.y + y, z = this.z + z)
    }

    override fun add(point: com.typewritermc.core.utils.point.Point) = add(point.x, point.y, point.z)

    override fun add(value: Double) = add(value, value, value)

    override fun plus(point: com.typewritermc.core.utils.point.Point) = add(point)

    override fun plus(value: Double) = add(value)

    override fun sub(x: Double, y: Double, z: Double): com.typewritermc.core.utils.point.Coordinate {
        return copy(x = this.x - x, y = this.y - y, z = this.z - z)
    }

    override fun sub(point: com.typewritermc.core.utils.point.Point) = sub(point.x, point.y, point.z)

    override fun sub(value: Double) = sub(value, value, value)

    override fun minus(point: com.typewritermc.core.utils.point.Point) = sub(point)

    override fun minus(value: Double) = sub(value)

    override fun mul(x: Double, y: Double, z: Double): com.typewritermc.core.utils.point.Coordinate {
        return copy(x = this.x * x, y = this.y * y, z = this.z * z)
    }

    override fun mul(point: com.typewritermc.core.utils.point.Point) = mul(point.x, point.y, point.z)

    override fun mul(value: Double) = mul(value, value, value)

    override fun times(point: com.typewritermc.core.utils.point.Point) = mul(point)

    override fun times(value: Double) = mul(value)

    override fun div(x: Double, y: Double, z: Double): com.typewritermc.core.utils.point.Coordinate {
        return copy(x = this.x / x, y = this.y / y, z = this.z / z)
    }

    override fun div(point: com.typewritermc.core.utils.point.Point) = div(point.x, point.y, point.z)

    override fun div(value: Double) = div(value, value, value)

    override fun lookAt(point: com.typewritermc.core.utils.point.Point): com.typewritermc.core.utils.point.Coordinate {
        val x = point.x - this.x
        val y = point.y - this.y
        val z = point.z - this.z

        val yaw = Math.toDegrees(atan2(x, z)).toFloat()
        val pitch = Math.toDegrees(atan2(y, sqrt(x.squared() + z.squared()))).toFloat()

        return copy(yaw = yaw, pitch = pitch)
    }

    override fun resetRotation(): com.typewritermc.core.utils.point.Coordinate = copy(yaw = 0f, pitch = 0f)

    override fun directionVector(): com.typewritermc.core.utils.point.Vector {
        val radYaw = Math.toRadians(yaw.toDouble())
        val radPitch = Math.toRadians(pitch.toDouble())
        val x = -cos(radPitch) * sin(radYaw)
        val y = -sin(radPitch)
        val z = cos(radPitch) * cos(radYaw)
        return com.typewritermc.core.utils.point.Vector(x, y, z)
    }
}

fun com.typewritermc.core.utils.point.Coordinate.toPosition(world: com.typewritermc.core.utils.point.World) =
    com.typewritermc.core.utils.point.Position(world, x, y, z, yaw, pitch)