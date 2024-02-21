package me.gabber235.typewriter.entries.entity

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.entries.SoundSourceEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry

@Tags("z_npc")
interface ZNPC : SpeakerEntry, SoundSourceEntry {
    val npcId: String
}