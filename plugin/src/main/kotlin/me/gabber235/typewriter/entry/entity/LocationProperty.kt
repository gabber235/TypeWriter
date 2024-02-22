package me.gabber235.typewriter.entry.entity

import com.github.retrooper.packetevents.protocol.world.Location
import me.gabber235.typewriter.entry.entries.EntityProperty
import java.util.*

data class LocationProperty(
    val world: UUID,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float,
) : EntityProperty {
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

    fun distanceSquared(other: LocationProperty): Double? {
        if (world != other.world) return null
        return toVector().distanceSquared(other.toVector())
    }

    fun distanceSquared(other: org.bukkit.Location): Double? {
        if (world != other.world.uid) return null
        return toVector().distanceSquared(other.toVector())
    }
}

fun org.bukkit.Location.toProperty(): LocationProperty {
    return LocationProperty(this)
}
