package com.typewritermc.engine.paper.entry.entries

import com.typewritermc.core.entries.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.TriggerEntry
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.matches
import dev.jorel.commandapi.CommandTree
import org.bukkit.entity.Player

@Tags("event")
interface EventEntry : TriggerEntry

interface CustomCommandEntry : EventEntry {
    @Help("The command to register. Do not include the leading slash.")
    val command: String

    fun CommandTree.builder()

    companion object
}

interface FireTriggerEventEntry : EventEntry

class Event(val player: Player, val triggers: List<EventTrigger>) {
    constructor(player: Player, vararg triggers: EventTrigger) : this(player, triggers.toList())

    operator fun contains(trigger: EventTrigger) = triggers.contains(trigger)

    operator fun contains(entry: Entry) = triggers.any { it.id == entry.id }


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
    fun canTriggerFor(player: Player): Boolean = true
}

data class EntryTrigger(val ref: Ref<out TriggerableEntry>) : EventTrigger {
    override val id: String = ref.id

    constructor(entry: TriggerableEntry) : this(entry.ref())

    override fun canTriggerFor(player: Player): Boolean = ref.get()?.criteria?.matches(player) ?: false
}

data object InteractionEndTrigger : EventTrigger {
    override val id: String
        get() = "system.interaction.end"

}