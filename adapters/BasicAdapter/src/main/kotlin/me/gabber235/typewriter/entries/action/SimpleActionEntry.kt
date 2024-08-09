package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry

@Entry("simple_action", "Simple action to modify facts", Colors.RED, "heroicons:bolt-16-solid")
/**
 * The `Simple Action` is an empty action that can be used to modify facts.
 *
 * ## How could this be used?
 *
 * This action can be useful in situations where you need to modify facts, or want to filter different actions based om some criteria,
 * but don't need to perform any additional actions.
 */
class SimpleActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : ActionEntry