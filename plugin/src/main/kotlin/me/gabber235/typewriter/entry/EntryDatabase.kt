package me.gabber235.typewriter.entry

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.adapters.customEditors
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.utils.*
import java.util.*
import kotlin.reflect.KClass

object EntryDatabase {
	private var pages: List<Page> = emptyList()
	private var entries: List<Entry> = emptyList()

	var facts = listOf<FactEntry>()
		private set
	private var entities = listOf<EntityEntry>()
	var events = listOf<EventEntry>()
		private set
	private var dialogue = listOf<DialogueEntry>()
	private var actions = listOf<ActionEntry>()
	internal var commandEvents = listOf<CustomCommandEntry>()
		private set

	fun init() {
		plugin.listen<TypewriterReloadEvent> { loadEntries() }
		loadEntries()
	}

	fun loadEntries() {
		val dir = plugin.dataFolder["pages"]
		if (!dir.exists()) {
			dir.mkdirs()
		}

		val gson = gson()

		val pages = dir.listFiles { file -> file.name.endsWith(".json") }?.mapNotNull { file ->
			val id = file.nameWithoutExtension
			val dialogueReader = JsonReader(file.reader())
			dialogueReader.parsePage(id, gson)
		} ?: emptyList()

		this.facts = pages.flatMap { it.entries.filterIsInstance<FactEntry>() }
		this.entities = pages.flatMap { it.entries.filterIsInstance<EntityEntry>() }
		this.events = pages.flatMap { it.entries.filterIsInstance<EventEntry>() }
		this.dialogue = pages.flatMap { it.entries.filterIsInstance<DialogueEntry>() }
		this.actions = pages.flatMap { it.entries.filterIsInstance<ActionEntry>() }

		val newCommandEvents = pages.flatMap { it.entries.filterIsInstance<CustomCommandEntry>() }
		this.commandEvents = CustomCommandEntry.refreshAndRegisterAll(newCommandEvents)

		this.entries = pages.flatMap { it.entries }
		this.pages = pages

		EntryListeners.register()

		plugin.logger.info("Loaded ${facts.size} facts, ${entities.size} entities, ${events.size} events, ${dialogue.size} dialogues, ${actions.size} actions, and ${commandEvents.size} commands.")
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
		customEditors.mapValues { it.value.serializer }.filterValues { it != null }.forEach {
			builder = builder.registerTypeAdapter(it.key.java, it.value)
		}

		return builder
			.create()
	}


	internal fun <T : Entry> findEntries(klass: KClass<T>, predicate: (T) -> Boolean): List<T> {
		return entries.asSequence().filterIsInstance(klass.java).filter(predicate).toList()
	}

	fun <E : Entry> findEntriesFromPage(klass: KClass<E>, pageId: String, filter: (E) -> Boolean): List<E> {
		return pages.firstOrNull { it.id == pageId }?.entries?.asSequence()?.filterIsInstance(klass.java)
			?.filter(filter)?.toList()
			?: emptyList()
	}

	internal fun <T : Entry> findEntry(klass: KClass<T>, predicate: (T) -> Boolean): T? {
		return entries.asSequence().filterIsInstance(klass.java).firstOrNull(predicate)
	}

	internal fun <T : Entry> findEntryById(kClass: KClass<T>, id: String): T? = findEntry(kClass) { it.id == id }
	internal fun getFact(id: String) = facts.firstOrNull { it.id == id }
	internal fun findFactByName(name: String) = facts.firstOrNull { it.name == name }
}

private fun JsonReader.parsePage(id: String, gson: Gson): Page? {
	return try {

		var page = Page(id)

		beginObject()
		while (hasNext()) {
			when (nextName()) {
				"entries" -> page = page.copy(entries = parseEntries(gson))
				else      -> skipValue()
			}
		}

		page
	} catch (e: Exception) {
		plugin.logger.warning("Failed to parse page: ${e.message}")
		null
	}
}

private fun JsonReader.parseEntries(gson: Gson): List<Entry> {
	val entries = mutableListOf<Entry>()

	beginArray()
	while (hasNext()) {
		val entry = parseEntry(gson) ?: continue
		entries.add(entry)
	}
	endArray()

	return entries
}

private fun JsonReader.parseEntry(gson: Gson): Entry? {
	return try {
		gson.fromJson(this, Entry::class.java)
	} catch (e: NonExistentSubtypeException) {
		val subtypeName = e.subtypeName
		plugin.logger.warning(
			"""
			|--------------------------------------------------------------------------
			|Failed to parse entry: $subtypeName is not a valid entry type. (skipping)
			|
			|This is either because an adapter is missing or due to having an outdated page entry. 
			|
			|Please report this on the TypeWriter Discord!
			|--------------------------------------------------------------------------
		""".trimMargin()
		)
		null
	} catch (e: Exception) {
		plugin.logger.warning("Failed to parse entry: ${e.message}")
		null
	}
}

data class Page(
	val id: String = "",
	val entries: List<Entry> = emptyList(),
)

fun Iterable<Criteria>.matches(playerUUID: UUID): Boolean = all {
	val entry = Query.findById<ReadableFactEntry>(it.fact)
	val fact = entry?.read(playerUUID)
	it.isValid(fact)
}