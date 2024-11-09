package com.typewritermc.basic.entries.dialogue

import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Colored
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.DialogueEntry
import com.typewritermc.engine.paper.entry.entries.SpeakerEntry
import java.time.Duration

@Entry("action_bar_dialogue", "An action bar dialogue", "#1E88E5", "fa6-solid:xmarks-lines")
/**
 * The `Action Bar Dialogue` is a dialogue that displays an animated message in the action bar.
 * It is similar to the Spoken Dialogue, but it displays the message in the action bar.
 *
 * ## How could this be used?
 * This action can be useful in a variety of situations. You can use it to create storylines, provide instructions to players, or create immersive roleplay experiences. The possibilities are endless!
 */
class ActionBarDialogueEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val speaker: Ref<SpeakerEntry> = emptyRef(),
    @Colored
    @Placeholder
    val text: String = "",
    @Help("The duration it takes to type out the message.")
    val duration: Duration = Duration.ZERO,
) : DialogueEntry