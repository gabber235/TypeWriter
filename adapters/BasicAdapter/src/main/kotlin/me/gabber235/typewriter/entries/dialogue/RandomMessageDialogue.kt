package me.gabber235.typewriter.entries.dialogue

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.MultiLine
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.utils.Icons

@Entry("random_message", "Display a random message from a list to a player", "#1c4da3", Icons.COMMENT)
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
    override val triggers: List<String> = emptyList(),
    override val speaker: String = "",
    @MultiLine
    @Colored
    @Placeholder
    @Help("The text to display to the player. One will be picked at random.")
    val messages: List<String> = emptyList(),
) : DialogueEntry