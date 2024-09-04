package com.typewritermc.example.entries.trigger

import com.google.gson.annotations.SerializedName
import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.CustomTriggeringActionEntry
import org.bukkit.entity.Player

//<code-block:custom_triggering_action_entry>
@Entry(
    "example_custom_triggering_action",
    "An example custom triggering entry.",
    Colors.RED,
    "material-symbols:touch-app-rounded"
)
class ExampleCustomTriggeringActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    @SerializedName("triggers")
    override val customTriggers: List<Ref<TriggerableEntry>> = emptyList(),
) : CustomTriggeringActionEntry {
    override fun execute(player: Player) {
        super.execute(player) // This will apply the modifiers.
        // Do something with the player
        player.triggerCustomTriggers() // Can be called later to trigger the next entries.
    }
}
//</code-block:custom_triggering_action_entry>