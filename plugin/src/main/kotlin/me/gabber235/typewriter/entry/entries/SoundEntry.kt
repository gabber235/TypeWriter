package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.StaticEntry
import net.kyori.adventure.sound.Sound

@Tags("sound_id")
interface SoundIdEntry : StaticEntry {
    val soundId: String
}

@Tags("sound_source")
interface SoundSourceEntry : StaticEntry {
    fun getEmitter(): Sound.Emitter
}