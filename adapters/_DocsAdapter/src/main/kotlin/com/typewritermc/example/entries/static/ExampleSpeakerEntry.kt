package com.typewritermc.example.entries.static

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.utils.Sound

//<code-block:speaker_entry>
@Entry("example_speaker", "An example speaker entry.", Colors.BLUE, "ic:round-spatial-audio-off")
class ExampleSpeakerEntry(
    override val id: String = "",
    override val name: String = "",
    override val displayName: String = "",
    override val sound: Sound = Sound.EMPTY,
) : SpeakerEntry
//</code-block:speaker_entry>