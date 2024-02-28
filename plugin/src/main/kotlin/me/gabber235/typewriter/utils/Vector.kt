package me.gabber235.typewriter.utils

import com.github.retrooper.packetevents.util.Vector3f

data class Vector(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
) {
    fun toPacketVector3f() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
}