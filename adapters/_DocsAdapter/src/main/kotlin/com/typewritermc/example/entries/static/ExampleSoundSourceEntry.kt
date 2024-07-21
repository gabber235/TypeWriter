package com.typewritermc.example.entries.static

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.SoundSourceEntry
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