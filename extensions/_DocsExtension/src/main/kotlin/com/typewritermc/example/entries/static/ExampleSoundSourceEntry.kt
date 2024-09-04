package com.typewritermc.example.entries.static

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.SoundSourceEntry
import net.kyori.adventure.sound.Sound

//<code-block:sound_source_entry>
@Entry("example_sound_source", "An example sound source entry.", Colors.BLUE, "ic:round-spatial-audio-off")
class ExampleSoundSourceEntry(
    override val id: String = "",
    override val name: String = "",
) : SoundSourceEntry {
    override fun getEmitter(): Sound.Emitter {
        // Return the emitter that should be used for the sound.
        // A bukkit entity can be used here.
        return Sound.Emitter.self()
    }
}
//</code-block:sound_source_entry>