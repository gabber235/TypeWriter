package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger

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
) : ActionEntry {
    override fun ActionTrigger.execute() {}
}