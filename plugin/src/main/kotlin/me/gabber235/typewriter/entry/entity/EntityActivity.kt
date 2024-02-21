package me.gabber235.typewriter.entry.entity

import org.bukkit.Location
import org.bukkit.entity.Player


interface ActivityCreator {
    fun create(player: Player?): EntityActivity
}

/**
 * Must be an immutable class
 */
interface EntityActivity {
    fun canActivate(currentLocation: Location): Boolean
    fun currentTask(currentLocation: Location): EntityTask

    fun primaryLocation(): Location
}

interface EntityTask {
    val location: Location
    fun tick()
    fun mayInterrupt(): Boolean
    fun isComplete(): Boolean
}