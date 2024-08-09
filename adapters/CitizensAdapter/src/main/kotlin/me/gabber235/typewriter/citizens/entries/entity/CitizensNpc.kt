package me.gabber235.typewriter.citizens.entries.entity

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.entries.SoundSourceEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry

@Tags("citizens_npc")
interface CitizensNpc : SoundSourceEntry, SpeakerEntry {
    val npcId: Int
}