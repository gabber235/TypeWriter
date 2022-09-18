package me.gabber235.typewriter.entry

import com.google.gson.annotations.SerializedName

data class SpeakerEntry(
	override val id: String = "",
	override val name: String = "",
	@SerializedName("display_name")
	val displayName: String = "",
	val sound: String = "",
) : Entry