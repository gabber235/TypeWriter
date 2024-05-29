package me.gabber235.typewriter.entry

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.adapters.customEditors
import me.gabber235.typewriter.database.Database
import me.gabber235.typewriter.entry.entries.CustomCommandEntry
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.entries.FactEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.events.PublishedBookEvent
import me.gabber235.typewriter.events.TypewriterReloadEvent
import me.gabber235.typewriter.logger
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.NonExistentSubtypeException
import me.gabber235.typewriter.utils.RuntimeTypeAdapterFactory
import me.gabber235.typewriter.utils.refreshAndRegisterAll
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import kotlin.reflect.KClass

interface EntryDatabase {
    val events: List<EventEntry>
    val facts: List<FactEntry>
    val commandEvents: List<CustomCommandEntry>

    fun initialize()
    fun loadEntries()

    fun <E : Entry> findEntries(klass: KClass<E>, predicate: (E) -> Boolean): List<E>

    fun <E : Entry> findEntriesFromPage(klass: KClass<E>, pageId: String, filter: (E) -> Boolean): List<E>

    fun <E : Entry> findEntry(klass: KClass<E>, predicate: (E) -> Boolean): E?

    fun <E : Entry> findEntryById(kClass: KClass<E>, id: String): E?
    fun getFact(id: String): FactEntry?
    fun findFactByName(name: String): FactEntry?
    fun getPageNames(type: PageType? = null): List<String>
}

class EntryDatabaseImpl : EntryDatabase, KoinComponent {
    private val database: Database by inject()
    private val entryListeners: EntryListeners by inject()

    private var pages: List<Page> = emptyList()
    private var entries: List<Entry> = emptyList()

    override var events = listOf<EventEntry>()
    override var facts = listOf<FactEntry>()
    override var commandEvents = listOf<CustomCommandEntry>()

    override fun initialize() {
        plugin.listen<TypewriterReloadEvent> { loadEntries() }
        plugin.listen<PublishedBookEvent> { loadEntries() }
    }

    override fun loadEntries() {
        val pages = database.loadPages()

        this.events = pages.flatMap { it.entries.filterIsInstance<EventEntry>() }
        this.facts = pages.flatMap { it.entries.filterIsInstance<FactEntry>() }

        val newCommandEvents = pages.flatMap { it.entries.filterIsInstance<CustomCommandEntry>() }
        this.commandEvents = CustomCommandEntry.refreshAndRegisterAll(newCommandEvents)

        this.entries = pages.flatMap { it.entries }
        this.pages = pages

        entryListeners.register()

        logger.info("Loaded ${entries.size} entries from ${pages.size} pages.")
    }

    override fun <T : Entry> findEntries(klass: KClass<T>, predicate: (T) -> Boolean): List<T> {
        return entries.asSequence().filterIsInstance(klass.java).filter(predicate).toList()
    }

    override fun <E : Entry> findEntriesFromPage(klass: KClass<E>, pageId: String, filter: (E) -> Boolean): List<E> {
        return pages.firstOrNull { it.id == pageId }?.entries?.asSequence()?.filterIsInstance(klass.java)
            ?.filter(filter)?.toList()
            ?: emptyList()
    }

    override fun <T : Entry> findEntry(klass: KClass<T>, predicate: (T) -> Boolean): T? {
        return entries.asSequence().filterIsInstance(klass.java).firstOrNull(predicate)
    }

    override fun <T : Entry> findEntryById(kClass: KClass<T>, id: String): T? = findEntry(kClass) { it.id == id }
    override fun getFact(id: String) = facts.firstOrNull { it.id == id }
    override fun findFactByName(name: String) = facts.firstOrNull { it.name == name }

    override fun getPageNames(type: PageType?): List<String> {
        return if (type == null) pages.map { it.id }
        else pages.filter { it.type == type }.map { it.id }
    }
}

fun JsonReader.parsePage(id: String, gson: Gson): Result<Page> {
    return try {
        var page = Page(id)

        beginObject()
        while (hasNext()) {
            when (nextName()) {
                "entries" -> page = page.copy(entries = parseEntries(gson))
                "type" -> page = page.copy(type = PageType.fromId(nextString()) ?: PageType.SEQUENCE)
                else -> skipValue()
            }
        }

        endObject()

        Result.success(page)
    } catch (e: Exception) {
        logger.warning("Failed to parse page: ${e.message}")
        Result.failure(e)
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

        logger.warning(
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
        logger.warning("Failed to parse entry: ${e.message} (${this})")
        null
    }
}

data class Page(
    val id: String = "",
    val entries: List<Entry> = emptyList(),
    val type: PageType = PageType.SEQUENCE,
)

enum class PageType(val id: String) {
    @SerializedName("sequence")
    SEQUENCE("sequence"),

    @SerializedName("static")
    STATIC("static"),

    @SerializedName("cinematic")
    CINEMATIC("cinematic"),
    ;

    companion object {
        fun fromId(id: String) = values().firstOrNull { it.id == id }
    }
}

fun Iterable<Criteria>.matches(playerUUID: UUID): Boolean = all {
    val entry = Query.findById<ReadableFactEntry>(it.fact)
    val fact = entry?.read(playerUUID)
    it.isValid(fact)
}

fun createEntryParserGson(adapterLoader: AdapterLoader): Gson {
    val entryFactory = RuntimeTypeAdapterFactory.of(Entry::class.java)

    val entries = adapterLoader.adapters.flatMap { it.entries }

    entries.groupingBy { it.name }.eachCount().filter { it.value > 1 }.forEach { (name, count) ->
        logger.warning("WARNING: Found $count entries with the name '$name'")
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