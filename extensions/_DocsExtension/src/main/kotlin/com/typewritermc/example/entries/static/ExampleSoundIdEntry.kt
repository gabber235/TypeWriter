package com.typewritermc.example.entries.static

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.SoundIdEntry

//<code-block:sound_id_entry>
@Entry("example_sound", "An example sound entry.", Colors.BLUE, "icon-park-solid:volume-up")
class ExampleSoundIdEntry(
    override val id: String = "",
    override val name: String = "",
    override val soundId: String = "",
) : SoundIdEntry
//</code-block:sound_id_entry>