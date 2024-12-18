package com.typewritermc.example.entries.trigger

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import org.bukkit.entity.Player

//<code-block:action_entry>
@Entry("example_action", "An example action entry.", Colors.RED, "material-symbols:touch-app-rounded")
class ExampleActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : ActionEntry {
    override fun ActionTrigger.execute() {
        // Do something with the player
    }
}
//</code-block:action_entry>

@Entry("example_action_manual_trigger", "An example action entry with a manual trigger.", Colors.RED, "material-symbols:touch-app-rounded")
class ExampleActionEntryManualTrigger(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
) : ActionEntry {
    //<code-block:action_entry_manual>
    override fun ActionTrigger.execute() {
        // This disables Typewriter's automatic triggering of the next entries,
        // and disables the automatic apply of the modifiers.
        disableAutomaticTriggering()

        // Now you can manually trigger the next entries.
        triggerManually()

        // Or if you want to specify which triggers to trigger, you can do so.
        triggers.filterIndexed { index, _ -> index % 2 == 0 }.triggerFor(player)

        // You can also manually apply the modifiers.
        applyModifiers()
    }
    //</code-block:action_entry_manual>
}