package me.gabber235.typewriter.entries.dialogue

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import java.time.Duration

@Entry("spoken", "Display a animated message to the player", "#1E88E5", "mingcute:message-4-fill")
/**
 * The `Spoken Dialogue Action` is an action that displays an animated message to the player. This action provides you with the ability to display a message with a specified speaker, text, and duration.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to create storylines, provide instructions to players, or create immersive roleplay experiences. The possibilities are endless!
 */
class SpokenDialogueEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val speaker: Ref<SpeakerEntry> = emptyRef(),
    @Placeholder
    @Colored
    @Help("The text to display to the player.")
    val text: String = "",
    @Help("The duration it takes to type out the message.")
    val duration: Duration = Duration.ZERO,
) : DialogueEntry