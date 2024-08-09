package com.typewritermc.example.entries.trigger

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.dialogue.DialogueMessenger
import me.gabber235.typewriter.entry.dialogue.MessengerState
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.utils.asMini
import org.bukkit.entity.Player
import java.time.Duration

//<code-block:dialogue_entry>
@Entry("example_dialogue", "An example dialogue entry.", Colors.BLUE, "material-symbols:chat-rounded")
class ExampleDialogueEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val speaker: Ref<SpeakerEntry> = emptyRef(),
    @MultiLine
    @Placeholder
    @Colored
    @Help("The text to display to the player.")
    val text: String = "",
) : DialogueEntry
//</code-block:dialogue_entry>

//<code-block:dialogue_messenger>
@Messenger(ExampleDialogueEntry::class)
class ExampleDialogueDialogueMessenger(player: Player, entry: ExampleDialogueEntry) :
    DialogueMessenger<ExampleDialogueEntry>(player, entry) {

    companion object : MessengerFilter {
        override fun filter(player: Player, entry: DialogueEntry): Boolean = true
    }

    // Called every game tick (20 times per second).
    // The cycle is a parameter that is incremented every tick, starting at 0.
    override fun tick(playTime: Duration) {
        super.tick(playTime)
        if (state != MessengerState.RUNNING) return

        player.sendMessage("${entry.speakerDisplayName}: ${entry.text}".parsePlaceholders(player).asMini())

        // When we want the dialogue to end, we can set the state to FINISHED.
        state = MessengerState.FINISHED
    }
}
//</code-block:dialogue_messenger>