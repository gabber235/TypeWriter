package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityProperty
import org.bukkit.Location

class ActivityManager(
    val activities: List<EntityActivity>,
) {
    private var activity: EntityActivity

    init {
        activity = activities.first()
    }

    val location: Location
        get() = activity.primaryLocation()

    val activeProperties: List<EntityProperty>
        get() = listOf(LocationProperty(location))

    fun tick() {}

    fun dispose() {
    }
}