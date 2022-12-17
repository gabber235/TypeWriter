package me.gabber235.typewriter.citizens

import me.gabber235.typewriter.entry.*
import net.citizensnpcs.api.persistence.Persist
import net.citizensnpcs.api.trait.Trait
import net.citizensnpcs.api.trait.TraitName

@TraitName("typewriter")
class TypewriterTrait : Trait("typewriter") {

	@Persist
	var identifier: String = ""
}
