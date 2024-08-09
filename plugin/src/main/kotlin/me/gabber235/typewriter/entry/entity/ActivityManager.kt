package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityProperty

class ActivityManager<Context : ActivityContext>(
    private val activity: EntityActivity<in Context>,
) {
    val location: LocationProperty
        get() = activity.currentLocation

    val activeProperties: List<EntityProperty>
        get() = activity.currentProperties

    fun initialize(context: Context) {
        activity.initialize(context)
    }

    fun tick(context: Context) {
        activity.tick(context)
    }

    fun dispose(context: Context) {
        activity.dispose(context)
    }
}