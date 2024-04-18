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
    fun tick(context: TaskContext)
    fun mayInterrupt(): Boolean
    fun isComplete(): Boolean

    fun dispose() {}
}

interface TaskContext {
    val isViewed: Boolean
}

class GroupTaskContext(
    val viewers: List<Player>,
) : TaskContext {
    override val isViewed: Boolean
        get() = viewers.isNotEmpty()
}

class IndividualTaskContext(
    val viewer: Player,
    override val isViewed: Boolean,
) : TaskContext