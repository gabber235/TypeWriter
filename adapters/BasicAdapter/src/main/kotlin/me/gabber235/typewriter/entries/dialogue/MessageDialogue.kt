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

@Entry("message", "Display a single message to the player", "#1c4da3", Icons.COMMENT)
/**
 * The `Message Dialogue Action` is an action that displays a single message to the player. This action provides you with the ability to show a message to the player in response to specific events.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations. You can use it to give the player information about their surroundings, provide them with tips, or add flavor to your adventure. The possibilities are endless!
 */
class MessageDialogueEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    override val speaker: String = "",
    @MultiLine
    @Placeholder
    @Colored
    @Help("The text to display to the player.")
    val text: String = "",
) : DialogueEntry