package me.gabber235.typewriter.citizens.entries.entities

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.utils.Icons

@Entry("reference_npc", "When the npc is not managed by TypeWriter", Colors.ORANGE, Icons.PERSON)
class ReferenceNpcEntry(
	override val id: String = "",
	override val name: String = "",
	override val displayName: String = "",
	override val sound: String = "",
	val npcId: Int = 0,
) : Npc