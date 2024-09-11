package com.typewritermc.engine.paper.entry.entries

import com.typewritermc.core.extension.annotations.Tags
import com.typewritermc.engine.paper.entry.PlaceholderEntry
import com.typewritermc.engine.paper.entry.StaticEntry
import net.kyori.adventure.sound.Sound
import org.bukkit.entity.Player

@Tags("sound_id")
interface SoundIdEntry : StaticEntry, PlaceholderEntry {
    val soundId: String

    override fun display(player: Player?): String? = soundId
}

@Tags("sound_source")
interface SoundSourceEntry : StaticEntry {
    fun getEmitter(player: Player): SoundEmitter
}

class SoundEmitter(val entityId: Int)