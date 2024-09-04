package com.typewritermc.basic.entries.dialogue

import com.typewritermc.core.entries.*
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Colored
import com.typewritermc.core.extension.annotations.MultiLine
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.entry.entries.SpeakerEntry

@Entry("message", "Display a single message to the player", "#1c4da3", "ic:baseline-comment-bank")
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
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val speaker: Ref<SpeakerEntry> = emptyRef(),
    @MultiLine
    @Placeholder
    @Colored
    val text: String = "",
) : DialogueEntry