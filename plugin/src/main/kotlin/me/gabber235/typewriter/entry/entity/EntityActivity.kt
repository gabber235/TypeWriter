package me.gabber235.typewriter.entry.entity

import org.bukkit.entity.Player


interface ActivityCreator {
    fun create(player: Player?): EntityActivity
}

/**
 * Must be an immutable class
 */
interface EntityActivity {
    fun canActivate(currentLocation: LocationProperty): Boolean
    fun currentTask(currentLocation: LocationProperty): EntityTask
}

interface EntityTask {
    val location: LocationProperty
    fun tick()
    fun mayInterrupt(): Boolean
    fun isComplete(): Boolean
}