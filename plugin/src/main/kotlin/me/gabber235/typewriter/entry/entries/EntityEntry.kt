package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Sound
import me.gabber235.typewriter.entry.StaticEntry

@Tags("entity")
interface EntityEntry : StaticEntry

@Tags("speaker")
interface SpeakerEntry : EntityEntry {
	val displayName: String

	@Sound
	val sound: String
}