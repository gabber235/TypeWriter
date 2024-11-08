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
) : Point, Rotatable {

    companion object {
        val ORIGIN = Coordinate(0.0, 0.0, 0.0, 0f, 0f)
    }

    override fun withX(x: Double): Coordinate = copy(x = x)

    override fun withY(y: Double): Coordinate = copy(y = y)

    override fun withZ(z: Double): Coordinate = copy(z = z)

    override fun withYaw(yaw: Float): Coordinate = copy(yaw = yaw)

    override fun withPitch(pitch: Float): Coordinate = copy(pitch = pitch)

    override fun rotateYaw(angle: Float): Coordinate = copy(yaw = this.yaw + angle)

    override fun rotatePitch(angle: Float): Coordinate = copy(pitch = this.pitch + angle)

    override fun rotate(yaw: Float, pitch: Float): Coordinate = copy(yaw = this.yaw + yaw, pitch = this.pitch + pitch)

    override fun add(x: Double, y: Double, z: Double): Coordinate {
        return copy(x = this.x + x, y = this.y + y, z = this.z + z)
    }

    override fun add(point: Point) = add(point.x, point.y, point.z)

    override fun add(value: Double) = add(value, value, value)

    override fun plus(point: Point) = add(point)

    override fun plus(value: Double) = add(value)

    override fun sub(x: Double, y: Double, z: Double): Coordinate {
        return copy(x = this.x - x, y = this.y - y, z = this.z - z)
    }

    override fun sub(point: Point) = sub(point.x, point.y, point.z)

    override fun sub(value: Double) = sub(value, value, value)

    override fun minus(point: Point) = sub(point)

    override fun minus(value: Double) = sub(value)

    override fun mul(x: Double, y: Double, z: Double): Coordinate {
        return copy(x = this.x * x, y = this.y * y, z = this.z * z)
    }

    override fun mul(point: Point) = mul(point.x, point.y, point.z)

    override fun mul(value: Double) = mul(value, value, value)

    override fun times(point: Point) = mul(point)

    override fun times(value: Double) = mul(value)

    override fun div(x: Double, y: Double, z: Double): Coordinate {
        return copy(x = this.x / x, y = this.y / y, z = this.z / z)
    }

    override fun div(point: Point) = div(point.x, point.y, point.z)

    override fun div(value: Double) = div(value, value, value)

    override fun lookAt(point: Point): Coordinate {
        val x = point.x - this.x
        val y = point.y - this.y
        val z = point.z - this.z

        val yaw = Math.toDegrees(atan2(x, z)).toFloat()
        val pitch = Math.toDegrees(atan2(y, sqrt(x.squared() + z.squared()))).toFloat()

        return copy(yaw = yaw, pitch = pitch)
    }

    override fun resetRotation(): Coordinate = copy(yaw = 0f, pitch = 0f)

    override fun directionVector(): com.typewritermc.core.utils.point.Vector {
        val radYaw = Math.toRadians(yaw.toDouble())
        val radPitch = Math.toRadians(pitch.toDouble())
        val x = -cos(radPitch) * sin(radYaw)
        val y = -sin(radPitch)
        val z = cos(radPitch) * cos(radYaw)
        return com.typewritermc.core.utils.point.Vector(x, y, z)
    }
}

fun Coordinate.toPosition(world: World) =
    Position(world, x, y, z, yaw, pitch)