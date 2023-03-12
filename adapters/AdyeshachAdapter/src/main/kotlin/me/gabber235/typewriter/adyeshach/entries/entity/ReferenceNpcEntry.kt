package me.gabber235.typewriter.adyeshach.entries.entity

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.utils.Icons

@Entry("reference_npc", "When the npc is not managed by TypeWriter", Colors.ORANGE, Icons.PERSON)
class ReferenceNpcEntry(
	override val id: String = "",
	override val name: String = "",
	override val displayName: String = "",
	override val sound: String = "",
	@Help("The id of the NPC in the Adyeshach plugin.")
	val npcId: String = "",
) : Npc