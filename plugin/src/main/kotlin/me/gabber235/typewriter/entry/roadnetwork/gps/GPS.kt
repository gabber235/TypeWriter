package me.gabber235.typewriter.entry.roadnetwork.gps

import org.bukkit.Location

interface GPS {
    suspend fun findPath(): Result<List<GPSEdge>>
}

data class GPSEdge(
    val start: Location,
    val end: Location,
    val weight: Double,
) {
    val isFastTravel: Boolean
        get() = weight == 0.0
}