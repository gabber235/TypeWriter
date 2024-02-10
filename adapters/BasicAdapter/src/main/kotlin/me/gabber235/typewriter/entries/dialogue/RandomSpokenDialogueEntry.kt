package me.gabber235.typewriter.entries.dialogue

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import java.time.Duration

@Entry(
    "random_spoken",
    "Display a random selected animated message to the player",
    "#1E88E5",
    "mingcute:message-4-fill"
)
/**
 * The `Random Spoken Dialogue` action displays a randomly selected animated message to the player.
 *
 * ## How could this be used?
 *
 * Let's say you have an NPC in your game who tells jokes to the player.
 * You could use the Random Spoken Dialogue action
 * to randomly select a joke from a list of possible jokes and have the NPC "tell"
 * it to the player using an animated message.
 */
class RandomSpokenDialogueEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val speaker: Ref<SpeakerEntry> = emptyRef(),
    @Placeholder
    @Colored
    @Help("The text to display to the player. One will be picked at random.")
    // A list of messages to display to the player. Every time the dialogue is triggered, one of these messages will be picked at random.
    val messages: List<String> = emptyList(),
    @Help("The duration it takes to type out the message.")
    val duration: Duration = Duration.ZERO,
) : DialogueEntry