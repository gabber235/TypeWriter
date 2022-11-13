package me.gabber235.typewriter.entries.entities

import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.entries.SpeakerEntry

// TODO: Move this to the citizens adapter
@Entry("npc", "An talking NPC")
data class NpcEntry(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("display_name")
	override val displayName: String = "",
	override val sound: String = "",
) : SpeakerEntry