package me.gabber235.typewriter.entry.entity

import lirand.api.extensions.server.mainWorld
import lirand.api.extensions.server.server
import me.gabber235.typewriter.entry.entries.EntityProperty
import me.gabber235.typewriter.logger

class ActivityManager(
    private val activities: List<EntityActivity>,
) {
    private var activity: EntityActivity? = null
    private var task: EntityTask? = null

    init {
        findNewTask()
    }

    val location: LocationProperty
        get() = task?.location ?: throw IllegalStateException("No task found!")

    val activeProperties: List<EntityProperty>
        get() = listOf(location)

    private fun findNewTask(): Boolean {
        val oldTask = task
        val newActivity = activities.firstOrNull {
            val location = oldTask?.location ?: it.primaryLocation()
            it.canActivate(location)
        }
        if (newActivity == null) {
            // If the old task is not complete, we can just keep doing it
            if (oldTask?.isComplete() == false) return false
            val location = oldTask?.location ?: server.mainWorld.spawnLocation.toProperty()
            task = IdleTask(location)
            logger.severe("No activity found! Assigning idle task.")
            return false
        }
        if (activity == null || oldTask == null) {
            activity = newActivity
            task = newActivity.currentTask(oldTask?.location ?: newActivity.primaryLocation())
            return true
        }

        if (activity != newActivity) {
            if (!oldTask.mayInterrupt()) return false
            activity = newActivity
            task = newActivity.currentTask(location)
            return true
        }

        if (!oldTask.isComplete()) return false
        task = newActivity.currentTask(location)
        return true
    }

    fun tick() {
        findNewTask()
        task?.tick()
    }

    fun dispose() {
    }
}

class IdleTask(override val location: LocationProperty) : EntityTask {
    override fun tick() {}
    override fun mayInterrupt(): Boolean = true

    override fun isComplete(): Boolean = false
}