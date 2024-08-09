package com.typewritermc.example.entries.static

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.SoundIdEntry

//<code-block:sound_id_entry>
@Entry("example_sound", "An example sound entry.", Colors.BLUE, "icon-park-solid:volume-up")
class ExampleSoundIdEntry(
    override val id: String = "",
    override val name: String = "",
    override val soundId: String = "",
) : SoundIdEntry
//</code-block:sound_id_entry>