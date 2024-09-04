package com.typewritermc.engine.paper.entry.entity

import com.typewritermc.core.utils.point.Point
import com.typewritermc.core.utils.point.Position
import com.typewritermc.core.utils.point.World
import com.typewritermc.engine.paper.entry.entries.EntityProperty
import com.typewritermc.engine.paper.utils.toPosition

class PositionProperty(
    world: World,
    x: Double,
    y: Double,
    z: Double,
    yaw: Float,
    pitch: Float,
) : EntityProperty, Position(world, x, y, z, yaw, pitch) {
    fun distanceSqrt(other: org.bukkit.Location): Double? {
        if (world.identifier != other.world.uid.toString()) return null
        return distanceSqrt(other.toPosition())
    }

    fun distanceSqrt(other: Position): Double? {
        if (world.identifier != other.world.identifier) return null
        return distanceSquared(other)
    }

    // Returns the x, z centered location
    fun mid(): PositionProperty {
        return copy(x = x.toInt() + 0.5, y = y.toInt().toDouble(), z = z.toInt() + 0.5)
    }

    override fun withX(x: Double) = copy(x = x)

    override fun withY(y: Double) = copy(y = y)

    override fun withZ(z: Double) = copy(z = z)

    override fun add(x: Double, y: Double, z: Double): PositionProperty {
        return copy(x = this.x + x, y = this.y + y, z = this.z + z)
    }

    override fun add(point: Point) = add(point.x, point.y, point.z)

    override fun add(value: Double) = add(value, value, value)

    override fun plus(point: Point) = add(point)

    override fun plus(value: Double) = add(value)

    override fun sub(x: Double, y: Double, z: Double) = copy(x = this.x - x, y = this.y - y, z = this.z - z)

    override fun sub(point: Point) = sub(point.x, point.y, point.z)

    override fun sub(value: Double) = sub(value, value, value)

    override fun minus(point: Point) = sub(point)

    override fun minus(value: Double) = sub(value)

    override fun mul(x: Double, y: Double, z: Double) = copy(x = this.x * x, y = this.y * y, z = this.z * z)

    override fun mul(point: Point) = mul(point.x, point.y, point.z)

    override fun mul(value: Double) = mul(value, value, value)

    override fun times(point: Point) = mul(point)

    override fun times(value: Double) = mul(value)

    override fun div(x: Double, y: Double, z: Double) = copy(x = this.x / x, y = this.y / y, z = this.z / z)

    override fun div(point: Point) = div(point.x, point.y, point.z)

    override fun div(value: Double) = div(value, value, value)

    override fun copy(
        world: World,
        x: Double,
        y: Double,
        z: Double,
        yaw: Float,
        pitch: Float,
    ): PositionProperty = PositionProperty(
        world,
        x,
        y,
        z,
        yaw,
        pitch
    )
}

fun org.bukkit.Location.toProperty(): PositionProperty {
    return PositionProperty(World(world.uid.toString()), x, y, z, yaw, pitch)
}

fun Position.toProperty(): PositionProperty {
    return PositionProperty(world, x, y, z, yaw, pitch)
}
