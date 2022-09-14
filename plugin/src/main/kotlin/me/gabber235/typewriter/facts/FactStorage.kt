package me.gabber235.typewriter.facts

import java.util.*

interface FactStorage {
	suspend fun loadFacts(uuid: UUID): Set<Fact>
	suspend fun storeFacts(uuid: UUID, facts: Set<Fact>)

	fun init() {}
	fun shutdown() {}
}