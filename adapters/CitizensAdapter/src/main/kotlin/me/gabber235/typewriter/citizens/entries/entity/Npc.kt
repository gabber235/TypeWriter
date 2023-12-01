package me.gabber235.typewriter.citizens.entries.entity

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.entries.SoundSourceEntry
import me.gabber235.typewriter.entry.entries.SpeakerEntry

@Tags("npc")
interface Npc : SpeakerEntry, SoundSourceEntry

