package me.gabber235.typewriter.entry.entity

import org.bukkit.entity.Player


interface ActivityCreator {
    fun create(context: TaskContext): EntityActivity
}

/**
 * Must be an immutable class
 */
interface EntityActivity {
    fun canActivate(context: TaskContext, currentLocation: LocationProperty): Boolean
    fun currentTask(context: TaskContext, currentLocation: LocationProperty): EntityTask
}

interface EntityTask {
    val location: LocationProperty
    fun tick(context: TaskContext)
    fun mayInterrupt(): Boolean
    fun isComplete(): Boolean

    fun dispose() {}
}

interface TaskContext {
    val isViewed: Boolean

    val viewers: List<Player>
}

class GroupTaskContext(
    override val viewers: List<Player>,
) : TaskContext {
    override val isViewed: Boolean
        get() = viewers.isNotEmpty()
}

class IndividualTaskContext(
    val viewer: Player,
    override val isViewed: Boolean,
) : TaskContext {
    override val viewers: List<Player>
        get() = listOf(viewer)
}

object EmptyTaskContext : TaskContext {
    override val isViewed: Boolean = false

    override val viewers: List<Player> = emptyList()
}