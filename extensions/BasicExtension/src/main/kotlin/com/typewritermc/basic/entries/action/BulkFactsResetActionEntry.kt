package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import com.typewritermc.engine.paper.entry.entries.WritableFactEntry
import org.bukkit.entity.Player

@Entry("bulk_facts_reset_action", "Reset a list of facts", Colors.RED, "mingcute:broom-fill")
/**
 * The `Bulk Facts Reset Action` is an action that resets a list of facts.
 *
 * ## How could this be used?
 * This is useful for when you need to reset a quest line.
 * But don't want to reset all the facts.
 */
class BulkFactsResetActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val facts: List<Ref<WritableFactEntry>> = emptyList(),
) : ActionEntry {
    override fun ActionTrigger.execute() {
        facts.forEach { it.get()?.write(player, 0) }
    }
}