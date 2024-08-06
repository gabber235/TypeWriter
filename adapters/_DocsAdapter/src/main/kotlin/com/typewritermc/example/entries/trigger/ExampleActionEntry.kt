package com.typewritermc.example.entries.trigger

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
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
    override fun execute(player: Player) {
        super.execute(player) // This will apply all the modifiers.
        // Do something with the player
    }
}
//</code-block:action_entry>