package com.typewritermc.example.entries.trigger

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.CustomTriggeringActionEntry
import org.bukkit.entity.Player

//<code-block:custom_triggering_action_entry>
@Entry("example_custom_triggering_action", "An example custom triggering entry.", Colors.RED, "material-symbols:touch-app-rounded")
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