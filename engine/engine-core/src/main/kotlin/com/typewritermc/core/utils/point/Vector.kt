package com.typewritermc.core.utils.point

import kotlin.math.sqrt

data class Vector(
    override val x: Double = 0.0,
    override val y: Double = 0.0,
    override val z: Double = 0.0,
) : Point {
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())

    companion object {
        val ZERO = Vector(0.0, 0.0, 0.0)
        const val EPSILON: Double = 0.000001
    }

    val lengthSquared: Double
        get() = x * x + y * y + z * z

    val length: Double
        get() = sqrt(lengthSquared)


    fun lerp(other: Vector, alpha: Double): Vector {
        return Vector(
            x = lerp(x, other.x, alpha),
            y = lerp(y, other.y, alpha),
            z = lerp(z, other.z, alpha),
        )
    }

    override fun withX(x: Double): Vector = copy(x = x)

    override fun withY(y: Double): Vector = copy(y = y)

    override fun withZ(z: Double): Vector = copy(z = z)

    override fun add(x: Double, y: Double, z: Double): Vector {
        return Vector(this.x + x, this.y + y, this.z + z)
    }

    override fun add(point: Point) = add(point.x, point.y, point.z)

    override fun add(value: Double) = add(value, value, value)

    override fun plus(point: Point) = add(point)

    override fun plus(value: Double) = add(value)

    override fun sub(x: Double, y: Double, z: Double): Vector {
        return Vector(this.x - x, this.y - y, this.z - z)
    }

    override fun sub(point: Point) = sub(point.x, point.y, point.z)

    override fun sub(value: Double) = sub(value, value, value)

    override fun minus(point: Point) = sub(point)

    override fun minus(value: Double) = sub(value)

    override fun mul(x: Double, y: Double, z: Double): Vector {
        return Vector(this.x * x, this.y * y, this.z * z)
    }

    override fun mul(point: Point) = mul(point.x, point.y, point.z)

    override fun mul(value: Double) = mul(value, value, value)

    override fun times(point: Point) = mul(point)

    override fun times(value: Double) = mul(value)

    override fun div(x: Double, y: Double, z: Double): Vector {
        return Vector(this.x / x, this.y / y, this.z / z)
    }

    override fun div(point: Point) = div(point.x, point.y, point.z)

    override fun div(value: Double) = div(value, value, value)

    fun normalize(): Vector {
        val length = length
        return if (length < EPSILON) {
            ZERO
        } else {
            div(length)
        }
    }

    private fun lerp(a: Double, b: Double, alpha: Double): Double {
        return a + alpha * (b - a)
    }

    fun mid(): Vector {
        return Vector(x.toInt() + 0.5, y.toInt().toDouble(), z.toInt() + 0.5)
    }
}

fun Point.toVector(): Vector {
    if (this is Vector) {
        return this
    }
    return Vector(x, y, z)
}
