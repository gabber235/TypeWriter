package me.gabber235.typewriter.entries.entity

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.entries.SoundSourceEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry

@Tags("fancy_npc")
interface FancyNpc : SpeakerEntry, SoundSourceEntry {
    val npcId: String
}
