package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.PlaceholderEntry
import me.gabber235.typewriter.entry.StaticEntry
import net.kyori.adventure.sound.Sound
import org.bukkit.entity.Player

@Tags("sound_id")
interface SoundIdEntry : StaticEntry, PlaceholderEntry {
    val soundId: String

    override fun display(player: Player?): String? = soundId
}

@Tags("sound_source")
interface SoundSourceEntry : StaticEntry {
    fun getEmitter(): Sound.Emitter
}