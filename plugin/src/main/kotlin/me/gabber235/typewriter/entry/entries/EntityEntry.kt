package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.entry.Entry

interface EntityEntry : Entry

interface SpeakerEntry : EntityEntry {
	val displayName: String
	val sound: String
}