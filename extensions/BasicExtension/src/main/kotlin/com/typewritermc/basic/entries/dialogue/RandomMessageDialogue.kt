package com.typewritermc.basic.entries.dialogue

import com.typewritermc.core.entries.*
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Colored
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.MultiLine
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.entry.entries.SpeakerEntry

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