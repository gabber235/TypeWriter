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
	 * Find all the entries for the given class.
	 */
	fun find() = findWhere { true }
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
