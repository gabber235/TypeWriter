package com.typewritermc.engine.paper.entry.entries

import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry

@Tags("dialogue")
interface DialogueEntry : TriggerableEntry {
    @Help("The speaker of the dialogue")
    val speaker: Ref<SpeakerEntry>

    val speakerDisplayName: String
        get() = speaker.get()?.displayName ?: ""
}

