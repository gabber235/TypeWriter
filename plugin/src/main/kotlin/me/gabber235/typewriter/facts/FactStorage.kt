package me.gabber235.typewriter.facts

import java.util.*


/**
 * A storage for facts. It is used to store facts and retrieve them.
 */
interface FactStorage {
	/**
	 * Load facts asynchronously for the given [player].
	 */
	suspend fun loadFacts(uuid: UUID): Set<Fact>

	/**
	 * Save the given [facts] for the given [player].
	 */
	suspend fun storeFacts(uuid: UUID, facts: Set<Fact>)

	/**
	 * Can initialize the storage. This is called when the plugin is enabled.
	 */
	fun init() {}

	/**
	 * Can shutdown the storage. This is called when the plugin is disabled.
	 */
	fun shutdown() {}
}