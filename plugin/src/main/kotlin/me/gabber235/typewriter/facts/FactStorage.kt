package me.gabber235.typewriter.facts


/**
 * A storage for facts. It is used to store facts and retrieve them.
 */
interface FactStorage {
    /**
     * Loads in all the facts from the storage.
     */
    suspend fun loadFacts(): Map<FactId, FactData>

    /**
     * Stores the given facts in the storage.
     * All the facts that are not in the given set should be removed from the storage.
     */
    suspend fun storeFacts(facts: Collection<Pair<FactId, FactData>>)

    /**
     * Can initialize the storage. This is called when the plugin is enabled.
     */
    fun init() {}

    /**
     * Can shutdown the storage. This is called when the plugin is disabled.
     */
    fun shutdown() {}
}