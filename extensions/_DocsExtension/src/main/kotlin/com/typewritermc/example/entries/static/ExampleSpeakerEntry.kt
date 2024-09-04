package com.typewritermc.example.entries.static

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.SpeakerEntry
import com.typewritermc.engine.paper.utils.Sound

//<code-block:speaker_entry>
@Entry("example_speaker", "An example speaker entry.", Colors.BLUE, "ic:round-spatial-audio-off")
class ExampleSpeakerEntry(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
) : SpeakerEntry
//</code-block:speaker_entry>