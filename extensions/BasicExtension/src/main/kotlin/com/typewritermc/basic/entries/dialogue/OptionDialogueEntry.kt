package com.typewritermc.basic.entries.dialogue

import com.typewritermc.core.entries.*
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Colored
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.entry.entries.SpeakerEntry
import java.time.Duration

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
    val text: String = "",
    val options: List<Option> = emptyList(),
    @Help("The duration it takes to type out the message. If the duration is zero, the message will be displayed instantly.")
    val duration: Duration = Duration.ZERO,
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