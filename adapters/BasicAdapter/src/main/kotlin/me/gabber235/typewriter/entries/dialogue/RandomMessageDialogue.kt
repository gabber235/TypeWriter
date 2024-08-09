package me.gabber235.typewriter.entries.dialogue

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry

@Entry("random_message", "Display a random message from a list to a player", "#1c4da3", "ic:baseline-comment-bank")
/**
 * The `Random Message Dialogue` action displays a random message from a list to the player. This action provides you with the ability to create interactive dialogues with randomized responses.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to create randomized NPC dialogue, quests with multiple randomized outcomes, or to add a level of unpredictability to your game. The possibilities are endless!
 */
class RandomMessageDialogueEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val speaker: Ref<SpeakerEntry> = emptyRef(),
    @MultiLine
    @Colored
    @Placeholder
    @Help("The text to display to the player. One will be picked at random.")
    val messages: List<String> = emptyList(),
) : DialogueEntry