package me.gabber235.typewriter.entry

import me.gabber235.typewriter.entry.entries.*
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
	infix fun findWhere(filter: (E) -> Boolean): List<E> = findWhere(klass, filter)

	/**
	 * Find the first entry that matches the given a filter
	 * @see findWhere
	 */
	infix fun firstWhere(filter: (E) -> Boolean): E? = firstWhere(klass, filter)


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

	infix fun findById(id: String): E? = findById(klass, id)

	companion object {
		/**
		 * Find all entries that match the given a filter
		 *
		 * Example:
		 * ```kotlin
		 * Query findWhere<SomeEntry> { it.someValue == "hello" }
		 * ```
		 *
		 * It can be used in combination with [triggerAllFor] to invoke all triggers for all entries that match the filter.
		 * Example:
		 * ```kotlin
		 * Query findWhere<SomeEntry> { it.someValue == "hello" } triggerAllFor player
		 * ```
		 *
		 * @param filter The filter to apply to the entries.
		 */
		inline infix fun <reified E : Entry> findWhere(noinline filter: (E) -> Boolean): List<E> {
			return findWhere(E::class, filter)
		}

		/**
		 * Find all entries that match the given a filter
		 *
		 * Example:
		 * ```kotlin
		 * Query.findWhere<SomeEntry>(SomeEntry::class) { it.someValue == "hello" }
		 * ```
		 *
		 * It can be used in combination with [triggerAllFor] to invoke all triggers for all entries that match the filter.
		 * Example:
		 * ```kotlin
		 * Query.findWhere<SomeEntry>(SomeEntry::class) { it.someValue == "hello" } triggerAllFor player
		 * ```
		 *
		 * @param filter The filter to apply to the entries.
		 */
		fun <E : Entry> findWhere(klass: KClass<E>, filter: (E) -> Boolean): List<E> {
			return EntryDatabase.findEntries(klass, filter)
		}

		/**
		 * Find the first entry that matches the given a filter
		 * @see findWhere
		 */
		inline infix fun <reified E : Entry> firstWhere(noinline filter: (E) -> Boolean): E? {
			return firstWhere(E::class, filter)
		}

		/**
		 * Find the first entry that matches the given a filter
		 * @see findWhere
		 */
		fun <E : Entry> firstWhere(klass: KClass<E>, filter: (E) -> Boolean): E? {
			return EntryDatabase.findEntry(klass, filter)
		}


		/**
		 * Find all the entries for the given class.
		 *
		 * Example:
		 * ```kotlin
		 * Query.find<SomeEntry>()
		 * ```
		 */
		inline fun <reified E : Entry> find() = findWhere<E> { true }

		/**
		 * Find all the entries for the given class.
		 *
		 * Example:
		 * ```kotlin
		 * Query.find<SomeEntry>(SomeEntry::class)
		 * ```
		 */
		fun <E : Entry> find(klass: KClass<E>) = findWhere(klass) { true }

		/**
		 * Find entry by [name].
		 *
		 * Example:
		 * ```kotlin
		 * Query findByName<SomeEntry> "someName"
		 * ```
		 */
		inline infix fun <reified E : Entry> findByName(name: String) = firstWhere<E> { it.name == name }

		/**
		 * Find entry by [name].
		 *
		 * Example:
		 * ```kotlin
		 * Query.findByName<SomeEntry>(SomeEntry::class, "someName")
		 * ```
		 */
		fun <E : Entry> findByName(klass: KClass<E>, name: String) = firstWhere(klass) { it.name == name }

		inline infix fun <reified E : Entry> findById(id: String): E? = findById(E::class, id)

		fun <E : Entry> findById(klass: KClass<E>, id: String): E? = EntryDatabase.findEntryById(klass, id)
	}
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
	val triggers = this.flatMap { it.triggers }.map { EntryTrigger(it) }
	InteractionHandler.triggerActions(player, triggers)
}

/**
 * Trigger all triggers for an entry.
 *
 * Example:
 * ```kotlin
 * val entry: SomeEntry = ...
 * entry triggerAllFor player
 * ```
 *
 * @param player The player to trigger the triggers for.
 */
infix fun <E : TriggerEntry> E.triggerAllFor(player: Player) {
	val triggers = this.triggers.map { EntryTrigger(it) }
	InteractionHandler.triggerActions(player, triggers)
}

/**
 * Trigger all triggers for all entries in a list.
 * This is a convenience method for [triggerAllFor] that takes a list of strings.
 *
 * Example:
 * ```kotlin
 * val triggers: List<String> = ...
 * triggers triggerAllFor player
 * ```
 *
 * @param player The player to trigger the triggers for.
 */
infix fun List<String>.triggerEntriesFor(player: Player) {
	val triggers = this.map { EntryTrigger(it) }
	InteractionHandler.triggerActions(player, triggers)
}

/**
 * Trigger a specific trigger for a player.
 *
 * Example:
 * ```kotlin
 * val trigger: EventTrigger = ...
 * trigger triggerFor player
 * ```
 *
 * @param player The player to trigger the trigger for.
 */
infix fun EventTrigger.triggerFor(player: Player) {
	InteractionHandler.triggerActions(player, listOf(this))
}

/**
 * If the player is not in a dialogue, trigger all triggers for all entries in a list.
 * If the player is in a dialogue, only trigger the [continueTrigger].
 *
 * This can be useful for actions that should not be triggered again if the player is already in a dialogue.
 * Like clicking on a npc to start a conversation. As we don't want to start the conversation again
 * if the player is already in a dialogue.
 *
 * Example:
 * ```kotlin
 * val entries: List<SomeEntry> = ...
 * entries.startDialogueWithOrTrigger(player, continueTrigger)
 * ```
 */
fun <E : TriggerEntry> List<E>.startDialogueWithOrTrigger(player: Player, continueTrigger: EventTrigger) {
	val triggers = this.flatMap { it.triggers }.map { EntryTrigger(it) }
	InteractionHandler.startDialogueWithOrTriggerEvent(player, triggers, continueTrigger)
}

/**
 * If the player is not in a dialogue, trigger all triggers for all entries in a list.
 * If the player is in a dialogue, it will trigger the next dialogue.
 *
 * This can be useful for actions that should not be triggered again if the player is already in a dialogue.
 * Like clicking on a npc to start a conversation. As we don't want to start the conversation again
 * if the player is already in a dialogue.
 *
 * Example:
 * ```kotlin
 * val entries: List<SomeEntry> = ...
 * entries startDialogueWithOrTrigger player
 * ```
 */
infix fun <E : TriggerEntry> List<E>.startDialogueWithOrNextDialogue(player: Player) =
	startDialogueWithOrTrigger(player, SystemTrigger.DIALOGUE_NEXT)


