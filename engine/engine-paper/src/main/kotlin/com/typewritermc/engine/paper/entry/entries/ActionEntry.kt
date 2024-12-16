package com.typewritermc.engine.paper.entry.entries

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.ref
import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.core.interaction.*
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.triggerEntriesFor
import com.typewritermc.engine.paper.entry.triggerFor
import com.typewritermc.engine.paper.facts.FactDatabase
import org.bukkit.entity.Player
import org.koin.java.KoinJavaComponent.get

@Tags("action")
interface ActionEntry : TriggerableEntry {
    fun ActionTrigger.execute()
}

class ActionTrigger(
    val player: Player,
    context: InteractionContext,
    val entry: ActionEntry,
): ContextModifier(context) {
    internal var automaticTriggering: Boolean = true
    internal var automaticModifiers: Boolean = true

    fun disableAutomaticTriggering() {
        automaticTriggering = false
        automaticModifiers = false
    }

    fun disableAutomaticModifiers() {
        automaticModifiers = false
    }

    fun triggerManually() {
        applyModifiers()
        entry.eventTriggers.triggerFor(player, context)
    }

    fun applyModifiers() {
        val factDatabase: FactDatabase = get(FactDatabase::class.java)
        factDatabase.modify(player, entry.modifiers)
    }

    fun List<Ref<out TriggerableEntry>>.triggerFor(player: Player) {
        this.triggerEntriesFor(player, context)
    }

    operator fun <T : Any> InteractionContext.set(key: EntryContextKey, value: T) {
        this[EntryInteractionContextKey<T>(entry.ref(), key)] = value
    }
}