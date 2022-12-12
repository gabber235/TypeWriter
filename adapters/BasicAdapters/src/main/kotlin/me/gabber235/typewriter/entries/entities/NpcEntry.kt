package me.gabber235.typewriter.entries.entities

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.SpeakerEntry
import me.gabber235.typewriter.utils.Icons

// TODO: Move this to the citizens adapter
@Entry("npc", "An talking NPC", Colors.ORANGE, Icons.PERSON)
data class NpcEntry(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("display_name")
	override val displayName: String = "",
	override val sound: String = "",
) : SpeakerEntry