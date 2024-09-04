package com.typewritermc.core.utils.point

import java.util.Objects
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

open class Position(
    val world: World,
    override val x: Double = 0.0,
    override val y: Double = 0.0,
    override val z: Double = 0.0,
    override val yaw: Float = 0f,
    override val pitch: Float = 0f,
) : Point, Rotatable {
    companion object {
        val ORIGIN = Position(World(""), 0.0, 0.0, 0.0, 0f, 0f)
    }

    override fun withX(x: Double): Position = copy(x = x)

    override fun withY(y: Double): Position = copy(y = y)

    override fun withZ(z: Double): Position = copy(z = z)

    override fun withYaw(yaw: Float): Position = copy(yaw = yaw)

    override fun withPitch(pitch: Float): Position = copy(pitch = pitch)

    override fun rotateYaw(angle: Float): Position = copy(yaw = this.yaw + angle)

    override fun rotatePitch(angle: Float): Position = copy(pitch = this.pitch + angle)

    override fun rotate(yaw: Float, pitch: Float): Position = copy(yaw = this.yaw + yaw, pitch = this.pitch + pitch)

    override fun add(x: Double, y: Double, z: Double): Position {
        return copy(x = this.x + x, y = this.y + y, z = this.z + z)
    }

    override fun add(point: Point) = add(point.x, point.y, point.z)

    override fun add(value: Double) = add(value, value, value)

    override fun plus(point: Point) = add(point)

    override fun plus(value: Double) = add(value)

    override fun sub(x: Double, y: Double, z: Double): Position {
        return copy(x = this.x - x, y = this.y - y, z = this.z - z)
    }

    override fun sub(point: Point) = sub(point.x, point.y, point.z)

    override fun sub(value: Double) = sub(value, value, value)

    override fun minus(point: Point) = sub(point)

    override fun minus(value: Double) = sub(value)

    override fun mul(x: Double, y: Double, z: Double): Position {
        return copy(x = this.x * x, y = this.y * y, z = this.z * z)
    }

    override fun mul(point: Point) = mul(point.x, point.y, point.z)

    override fun mul(value: Double) = mul(value, value, value)

    override fun times(point: Point) = mul(point)

    override fun times(value: Double) = mul(value)

    override fun div(x: Double, y: Double, z: Double): Position {
        return copy(x = this.x / x, y = this.y / y, z = this.z / z)
    }

    override fun div(point: Point) = div(point.x, point.y, point.z)

    override fun div(value: Double) = div(value, value, value)

    override fun lookAt(point: Point): Position {
        val x = point.x - this.x
        val y = point.y - this.y
        val z = point.z - this.z

        val yaw = Math.toDegrees(atan2(x, z)).toFloat()
        val pitch = Math.toDegrees(atan2(y, sqrt(x.squared() + z.squared()))).toFloat()

        return copy(yaw = yaw, pitch = pitch)
    }

    override fun resetRotation(): Position = copy(yaw = 0f, pitch = 0f)

    override fun directionVector(): Vector {
        val radYaw = Math.toRadians(yaw.toDouble())
        val radPitch = Math.toRadians(pitch.toDouble())
        val x = -cos(radPitch) * sin(radYaw)
        val y = -sin(radPitch)
        val z = cos(radPitch) * cos(radYaw)
        return Vector(x, y, z)
    }

    fun isInRange(point: Position, range: Double): Boolean {
        if (this.world != point.world) return false
        return isInRange(point.x, point.y, point.z, range)
    }

    open fun copy(
        world: World = this.world,
        x: Double = this.x,
        y: Double = this.y,
        z: Double = this.z,
        yaw: Float = this.yaw,
        pitch: Float = this.pitch,
    ): Position {
        return Position(world, x, y, z, yaw, pitch)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Position) return false

        if (world != other.world) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (yaw != other.yaw) return false
        if (pitch != other.pitch) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(world, x, y, z, yaw, pitch)
    override fun toString(): String {
        return "Position(world=$world, x=$x, y=$y, z=$z, yaw=$yaw, pitch=$pitch)"
    }
}
