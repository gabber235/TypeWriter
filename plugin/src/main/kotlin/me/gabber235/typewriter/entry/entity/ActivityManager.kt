package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.EntityInstanceEntry
import me.gabber235.typewriter.entry.entries.EntityProperty
import org.bukkit.Location

class ActivityManager(
    instanceRef: Ref<out EntityInstanceEntry>,
    private val activities: List<EntityActivity>,
    spawnLocation: Location,
) {
    private var activity: EntityActivity? = null
    private var task: EntityTask = IdleTask(spawnLocation.toProperty())
        set(value) {
            field.dispose()
            field = value
        }

    init {
        findNewTask(EmptyTaskContext(instanceRef))
    }

    val location: LocationProperty
        get() = task.location

    val activeProperties: List<EntityProperty>
        get() = listOf(location)

    private fun findNewTask(context: TaskContext): Boolean {
        val newActivity = activities.firstOrNull {
            it.canActivate(context, location)
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
            task = newActivity.currentTask(context, location)
            return true
        }

        if (activity != newActivity) {
            if (!task.mayInterrupt()) return false
            activity = newActivity
            task = newActivity.currentTask(context, location)
            return true
        }

        if (!task.isComplete()) return false
        task = newActivity.currentTask(context, location)
        return true
    }

    fun tick(context: TaskContext) {
        findNewTask(context)
        task.tick(context)
    }

    fun dispose() {
    }
}

class IdleTask(override val location: LocationProperty) : EntityTask {
    override fun tick(context: TaskContext) {}
    override fun mayInterrupt(): Boolean = true

    override fun isComplete(): Boolean = false
}

abstract class FilterActivity(
    private val children: List<EntityActivity>,
) : EntityActivity {
    override fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean {
        return children.any { it.canActivate(context, currentLocation) }
    }

    override fun currentTask(context: TaskContext, currentLocation: LocationProperty): EntityTask {
        return children.firstOrNull { it.canActivate(context, currentLocation) }?.currentTask(context, currentLocation)
            ?: IdleTask(currentLocation)
    }
}