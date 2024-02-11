package me.gabber235.typewriter.entries.dialogue

import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Colored
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Placeholder
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry

@Entry("option", "Display a list of options to the player", "#4CAF50", "fa6-solid:list")
/**
 * The `Option Dialogue` action displays a list of options to the player to choose from. This action provides you with the ability to give players choices that affect the outcome of the game.
 *
 * ## How could this be used?
 *
 * This action can be useful in a variety of situations, such as presenting the player with dialogue choices that determine the course of a story or offering the player a choice of rewards for completing a quest.
 */
class OptionDialogueEntry(
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
    @Help("The options for the player to choose from.")
    val options: List<Option> = emptyList(),
) : DialogueEntry

data class Option(
    @Help("Text for this option.")
    val text: String = "",
    @Help("The criteria that must be met for this option to show.")
    val criteria: List<Criteria> = emptyList(),
    @Help("The modifiers to apply when this option is chosen.")
    val modifiers: List<Modifier> = emptyList(),
    @Help("The triggers to fire when this option is chosen.")
    val triggers: List<Ref<TriggerableEntry>> = emptyList()
)