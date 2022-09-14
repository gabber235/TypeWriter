package me.gabber235.typewriter.extensions.npc

import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.trait.TraitInfo

object NpcHandler {

	fun init() {
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TypeWriterTrait::class.java))
	}
}