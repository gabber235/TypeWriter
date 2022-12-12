package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.entry.StaticEntry

@Tags("entity")
interface EntityEntry : StaticEntry

interface SpeakerEntry : EntityEntry {
	val displayName: String
	val sound: String
}