package me.gabber235.typewriter.entry

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.adapters.customEditors
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.utils.RuntimeTypeAdapterFactory
import me.gabber235.typewriter.utils.get
import kotlin.reflect.KClass

object EntryDatabase {
	private var entries: List<Entry> = emptyList()

	var facts = listOf<FactEntry>()
		private set
	private var entities = listOf<EntityEntry>()
	var events = listOf<EventEntry>()
		private set
	private var dialogue = listOf<DialogueEntry>()
	private var actions = listOf<ActionEntry>()

	fun loadEntries() {
		val dir = plugin.dataFolder["pages"]
		if (!dir.exists()) {
			dir.mkdirs()
		}

		val gson = gson()

		val pages = dir.listFiles { file -> file.name.endsWith(".json") }?.map { file ->
			val dialogueReader = JsonReader(file.reader())
			dialogueReader.parsePage(gson)
		}

		this.facts = pages?.flatMap { it.entries.filterIsInstance<FactEntry>() } ?: listOf()
		this.entities = pages?.flatMap { it.entries.filterIsInstance<EntityEntry>() } ?: listOf()
		this.events = pages?.flatMap { it.entries.filterIsInstance<EventEntry>() } ?: listOf()
		this.dialogue = pages?.flatMap { it.entries.filterIsInstance<DialogueEntry>() } ?: listOf()
		this.actions = pages?.flatMap { it.entries.filterIsInstance<ActionEntry>() } ?: listOf()

		this.entries = pages?.flatMap { it.entries } ?: listOf()

		EntryListeners.register()

		println("Loaded ${facts.size} fact, ${entities.size} entities, ${events.size} event, ${dialogue.size} dialogue and ${actions.size} action entries")
	}

	fun gson(): Gson {
		val entryFactory = RuntimeTypeAdapterFactory.of(Entry::class.java)

		val entries = AdapterLoader.getAdapterData().flatMap { it.entries }

		entries.groupingBy { it.name }.eachCount().filter { it.value > 1 }.forEach { (name, count) ->
			plugin.logger.warning("WARNING: Found $count entries with the name '$name'")
		}

		entries.forEach {
			entryFactory.registerSubtype(it.clazz, it.name)
		}

		var builder = GsonBuilder()
			.registerTypeAdapterFactory(entryFactory)

		customEditors.mapValues { it.value.deserializer }.filterValues { it != null }.forEach {
			builder = builder.registerTypeAdapter(it.key.java, it.value)
		}

		return builder
			.create()
	}


	internal fun <T : Entry> findEntries(klass: KClass<T>, predicate: (T) -> Boolean): List<T> {
		return entries.filterIsInstance(klass.java).filter(predicate)
	}

	internal fun <T : Entry> findEntry(klass: KClass<T>, predicate: (T) -> Boolean): T? {
		return entries.filterIsInstance(klass.java).firstOrNull(predicate)
	}

	@Deprecated("Old Api, use Query", ReplaceWith("Query(klass) findWhere predicate"))
	fun <T : EventEntry> findEventEntries(klass: KClass<T>, predicate: (T) -> Boolean): List<T> {
		return events.filterIsInstance(klass.java).filter(predicate)
	}

	internal fun findDialogue(trigger: String, facts: Set<Fact>): DialogueEntry? {
		val rules = dialogue.filter { trigger in it.triggeredBy }.sortedByDescending { it.criteria.size }
		return rules.find { rule -> rule.criteria.matches(facts) }
	}

	internal fun findActions(trigger: String, facts: Set<Fact>): List<ActionEntry> {
		return actions.filter { trigger in it.triggeredBy }.filter { rule -> rule.criteria.matches(facts) }
	}

	internal fun getEntity(id: String) = entities.firstOrNull { it.id == id }

	@JvmName("getEntityWithType")
	internal inline fun <reified E : EntityEntry> getEntity(id: String) = getEntity(id) as? E

	@Deprecated("Old Api, use Query", ReplaceWith("Query(klass) findByName name"))
	fun findEntityByName(name: String) = entities.firstOrNull { it.name == name }

	@JvmName("findEntityByNameWithType")
	@Deprecated("Old Api, use Query", ReplaceWith("Query(E::class) findByName name"))
	inline fun <reified E : EntityEntry> findEntityByName(name: String) = (Query(E::class) findByName name)

	fun getFact(id: String) = facts.firstOrNull { it.id == id }
	fun findFactByName(name: String) = facts.firstOrNull { it.name == name }

	fun getAllTriggers() = listOf(
		*events.toTypedArray(),
		*dialogue.toTypedArray(),
		*actions.toTypedArray()
	).flatMap { entry ->
		val triggers = entry.triggers.toMutableList()
		if (entry is RuleEntry) {
			triggers += entry.triggeredBy
		}
		// TODO Fix this in a better way, such that Adapters can change this.
//		if (entry is OptionDialogueEntry) {
//			triggers += entry.options.flatMap { it.triggers }
//		}

		triggers
	}
}

private fun JsonReader.parsePage(gson: Gson): Page =
	gson.fromJson(this, Page::class.java)

class Page(
	val entries: List<Entry>,
)

fun Iterable<Criteria>.matches(facts: Set<Fact>): Boolean = all { it.isValid(facts) }