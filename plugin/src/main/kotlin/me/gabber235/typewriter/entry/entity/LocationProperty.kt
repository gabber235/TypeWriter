package me.gabber235.typewriter.entry.entity

import com.github.retrooper.packetevents.protocol.world.Location
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.utils.Point
import java.util.*

data class LocationProperty(
    val world: UUID,
    override val x: Double,
    override val y: Double,
    override val z: Double,
    val yaw: Float,
    val pitch: Float,
) : EntityProperty, Point {
    constructor(location: org.bukkit.Location) : this(
        location.world.uid,
        location.x,
        location.y,
        location.z,
        location.yaw,
        location.pitch,
    )

    val bukkitWorld get() = org.bukkit.Bukkit.getWorld(world)

    fun toLocation(): org.bukkit.Location {
        return org.bukkit.Location(org.bukkit.Bukkit.getWorld(world), x, y, z, yaw, pitch)
    }

    fun toPacketLocation(): Location {
        return Location(x, y, z, yaw, pitch)
    }

    fun toVector(): org.bukkit.util.Vector {
        return org.bukkit.util.Vector(x, y, z)
    }

    fun distanceSqrt(other: LocationProperty): Double? {
        if (world != other.world) return null
        return toVector().distanceSquared(other.toVector())
    }

    fun distanceSqrt(other: org.bukkit.Location): Double? {
        if (world != other.world.uid) return null
        return toVector().distanceSquared(other.toVector())
    }

    // Returns the x, z centered location
    fun mid(): LocationProperty {
        return copy(x = x.toInt() + 0.5, y = y.toInt().toDouble(), z = z.toInt() + 0.5)
    }

    override fun withX(x: Double): Point = copy(x = x)

    override fun withY(y: Double): Point = copy(y = y)

    override fun withZ(z: Double): Point = copy(z = z)

    override fun add(x: Double, y: Double, z: Double): LocationProperty {
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

    override fun mul(point: Point)  = mul(point.x, point.y, point.z)

    override fun mul(value: Double) = mul(value, value, value)

    override fun times(point: Point)  = mul(point)

    override fun times(value: Double) = mul(value)

    override fun div(x: Double, y: Double, z: Double) = copy(x = this.x / x, y = this.y / y, z = this.z / z)

    override fun div(point: Point) = div(point.x, point.y, point.z)

    override fun div(value: Double) = div(value, value, value)
}

fun org.bukkit.Location.toProperty(): LocationProperty {
    return LocationProperty(this)
}
