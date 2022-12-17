package me.gabber235.typewriter.entry

import me.gabber235.typewriter.interaction.InteractionHandler
import org.bukkit.entity.Player
import kotlin.reflect.KClass

/**
 * A query can be used to search for entries.
 * This is useful for events where you want to invoke all triggers for a specific entry given a query.
 *
 * @param klass The class of the entry.
 */
class Query<E : Entry>(private val klass: KClass<E>) {

	/**
	 * Find all entries that match the given a filter
	 *
	 * Example:
	 * ```kotlin
	 * val query: Query<SomeEntry> = ...
	 * query findWhere { it.someValue == "hello" }
	 * ```
	 *
	 * It can be used in combination with [triggerAllFor] to invoke all triggers for all entries that match the filter.
	 * Example:
	 * ```kotlin
	 * val query: Query<SomeEntry> = ...
	 * query findWhere { it.someValue == "hello" } triggerAllFor player
	 * ```
	 *
	 * @param filter The filter to apply to the entries.
	 */
	infix fun findWhere(filter: (E) -> Boolean): List<E> {
		return EntryDatabase.findEntries(klass, filter)
	}

	/**
	 * Find the first entry that matches the given a filter
	 * @see findWhere
	 */
	infix fun firstWhere(filter: (E) -> Boolean): E? {
		return EntryDatabase.findEntry(klass, filter)
	}


	/**
	 * Find all the entries for the given class.
	 *
	 * Example:
	 * ```kotlin
	 * val query: Query<SomeEntry> = ...
	 * query.find()
	 * ```
	 */
	fun find() = findWhere { true }

	/**
	 * Find entry by [name].
	 *
	 * Example:
	 * ```kotlin
	 * val query: Query<SomeEntry> = ...
	 * query findByName "someName"
	 * ```
	 */
	infix fun findByName(name: String) = firstWhere { it.name == name }
}

/**
 * Trigger all triggers for all entries in a list.
 *
 * Example:
 * ```kotlin
 * val entries: List<SomeEntry> = ...
 * entries triggerAllFor player
 * ```
 *
 * @param player The player to trigger the triggers for.
 */
infix fun <E : TriggerEntry> List<E>.triggerAllFor(player: Player) {
	val triggers = this.flatMap { it.triggers }
	InteractionHandler.startInteractionAndTrigger(player, triggers)
}

/**
 * If the player is not in an interaction, trigger all triggers for all entries in a list.
 * If the player is in an interaction, only trigger the [continueTrigger].
 *
 * This can be useful for actions that should not be triggered again if the player is already in an interaction.
 * Like clicking on a npc to start a conversation. As we don't want to start the conversation again
 * if the player is already in an interaction.
 *
 * Example:
 * ```kotlin
 * val entries: List<SomeEntry> = ...
 * entries.startInteractionWithOrTrigger(player, continueTrigger)
 * ```
 */
fun <E : TriggerEntry> List<E>.startInteractionWithOrTrigger(player: Player, continueTrigger: String) {
	val triggers = this.flatMap { it.triggers }
	InteractionHandler.startInteractionWithOrTriggerEvent(player, triggers, continueTrigger)
}
