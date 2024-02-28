package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.entries.EntityProperty
import org.bukkit.Location

class ActivityManager(
    private val activities: List<EntityActivity>,
    spawnLocation: Location,
) {
    private var activity: EntityActivity? = null
    private var task: EntityTask = IdleTask(spawnLocation.toProperty())

    init {
        findNewTask()
    }

    val location: LocationProperty
        get() = task.location

    val activeProperties: List<EntityProperty>
        get() = listOf(location)

    private fun findNewTask(): Boolean {
        val newActivity = activities.firstOrNull {
            it.canActivate(location)
        }
        if (newActivity == null) {
            // If the old task is not complete, we can just keep doing it
            if (!task.isComplete()) return false
            activity = null
            task = IdleTask(location)
            return false
        }
        if (activity == null) {
            activity = newActivity
            task = newActivity.currentTask(location)
            return true
        }

        if (activity != newActivity) {
            if (!task.mayInterrupt()) return false
            activity = newActivity
            task = newActivity.currentTask(location)
            return true
        }

        if (!task.isComplete()) return false
        task = newActivity.currentTask(location)
        return true
    }

    fun tick() {
        findNewTask()
        task.tick()
    }

    fun dispose() {
    }
}

class IdleTask(override val location: LocationProperty) : EntityTask {
    override fun tick() {}
    override fun mayInterrupt(): Boolean = true

    override fun isComplete(): Boolean = false
}