package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.StaticEntry

@Tags("sound_id")
interface SoundIdEntry : StaticEntry {
    val soundId: String
}