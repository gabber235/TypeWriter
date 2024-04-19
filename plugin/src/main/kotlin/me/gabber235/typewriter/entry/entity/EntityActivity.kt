package me.gabber235.typewriter.entry.entity

import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.entries.EntityInstanceEntry
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
    val instanceRef: Ref<out EntityInstanceEntry>
    val isViewed: Boolean

    val viewers: List<Player>
}

class GroupTaskContext(
    override val instanceRef: Ref<out EntityInstanceEntry>,
    override val viewers: List<Player>,
) : TaskContext {
    override val isViewed: Boolean
        get() = viewers.isNotEmpty()
}

class IndividualTaskContext(
    override val instanceRef: Ref<out EntityInstanceEntry>,
    val viewer: Player,
    override val isViewed: Boolean,
) : TaskContext {
    override val viewers: List<Player>
        get() = listOf(viewer)
}

class EmptyTaskContext(
    override val instanceRef: Ref<out EntityInstanceEntry>,
) : TaskContext {
    override val isViewed: Boolean = false

    override val viewers: List<Player> = emptyList()
}