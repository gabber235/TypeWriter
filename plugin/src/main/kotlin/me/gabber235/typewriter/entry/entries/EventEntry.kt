package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.content.ContentContext
import me.gabber235.typewriter.content.ContentMode
import me.gabber235.typewriter.entry.*
import org.bukkit.entity.Player

@Tags("event")
interface EventEntry : TriggerEntry

interface CustomCommandEntry : EventEntry {
    @Help("The command to register. Do not include the leading slash.")
    val command: String

    fun filter(player: Player, commandLabel: String, args: Array<out String>): CommandFilterResult =
        CommandFilterResult.Success

    fun execute(player: Player, commandLabel: String, args: Array<out String>) {
        triggerAllFor(player)
    }

    sealed interface CommandFilterResult {
        data object Success : CommandFilterResult
        data object Failure : CommandFilterResult
        data object FailureWithDefaultMessage : CommandFilterResult
        data class FailureWithMessage(val message: String) : CommandFilterResult
    }

    companion object
}

class Event(val player: Player, val triggers: List<EventTrigger>) {
    constructor(player: Player, vararg triggers: EventTrigger) : this(player, triggers.toList())

    operator fun contains(trigger: EventTrigger) = triggers.contains(trigger)

    operator fun contains(entry: Entry) = EntryTrigger(entry.id) in triggers


    fun merge(other: Event?): Event {
        if (other == null) return this
        if (player.uniqueId != other.player.uniqueId) return this
        return Event(player, triggers + other.triggers)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Event) return false

        if (triggers != other.triggers) return false
        return player.uniqueId == other.player.uniqueId
    }

    override fun hashCode(): Int {
        var result = triggers.hashCode()
        result = 31 * result + player.hashCode()
        return result
    }

    override fun toString(): String {
        return "Event(player=${player.name}, triggers=$triggers)"
    }

    fun distinct(): Event = Event(player, triggers.distinct())
}

interface EventTrigger {
    val id: String
}

data class EntryTrigger(override val id: String) : EventTrigger {
    constructor(entry: Entry) : this(entry.id)
    constructor(reference: Ref<out Entry>) : this(reference.id)
}

enum class SystemTrigger : EventTrigger {
    DIALOGUE_NEXT,
    DIALOGUE_END,
    CINEMATIC_END,
    CONTENT_POP,
    CONTENT_END,
    ;

    override val id: String
        get() = "system.${name.lowercase().replace('_', '.')}"

    override fun toString(): String {
        return "SystemTrigger(id='$id')"
    }
}

data class CinematicStartTrigger(
    val pageId: String,
    val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val override: Boolean = false,
) : EventTrigger {
    override val id: String
        get() = "system.cinematic.start.$pageId"
}

open class ContentModeTrigger(
    val context: ContentContext,
    val mode: ContentMode,
) : EventTrigger {
    override val id: String
        get() = "content.${mode::class.simpleName}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ContentModeTrigger) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

/**
 * Swaps the content mode to the given mode.
 *
 * If no content mode is currently active, this will start a new context.
 */
class ContentModeSwapTrigger(
    context: ContentContext,
    mode: ContentMode,
) : ContentModeTrigger(context, mode) {
    override val id: String
        get() = "content.swap.${mode::class.simpleName}"
}