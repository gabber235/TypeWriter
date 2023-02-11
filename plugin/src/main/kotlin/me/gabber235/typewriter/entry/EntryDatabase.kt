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
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
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
	// used for registering commands
	private var commandEvents = listOf<EventEntry>()

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

		this.commandEvents = this.events.filter {
			it::class.simpleName == "RunCommandEventEntry"
		}

		registerCommands()
		EntryListeners.register()

		println("Loaded ${facts.size} facts, ${entities.size} entities, ${events.size} events, ${dialogue.size} dialogues, ${actions.size} actions, and ${commandEvents.size} commands.")
	}

	private fun registerCommands() {
		// https://www.spigotmc.org/threads/tutorial-how-to-register-unregister-custom-commands-at-runtime.493956/
		commandEvents.forEach { event ->
			val aliases = mutableListOf<String>()
			aliases.add(event.command)

			val usage = "/${event.command}"
			val description = "description" // TODO: add description to entry

			val command = object : BukkitCommand(event.command, description, usage, aliases) {
				override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
					// executes on its own
					return true
				}
			}

			plugin.getCommand(event.command)?.unregister(getCommandMap())
			getCommandMap().register(plugin.name, command)

			println("Registered command ${event.command} for ${event.name} (${event.id})")
		}
	}

	private fun getCommandMap(): CommandMap {
		var map: CommandMap = Bukkit.getServer().commandMap

		try {
			val server = Bukkit.getServer()
			val serverClass = server.javaClass
			val commandMapField = serverClass.getDeclaredField("commandMap")
			commandMapField.isAccessible = true
			map = commandMapField.get(server) as CommandMap
		} catch (e: Exception) {
			e.printStackTrace()
		}

		return map
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
		return entries.filterIsInstance(klass.java).filter(predicate)
	}

	internal fun <T : Entry> findEntry(klass: KClass<T>, predicate: (T) -> Boolean): T? {
		return entries.filterIsInstance(klass.java).firstOrNull(predicate)
	}

	internal fun <T : Entry> findEntryById(kClass: KClass<T>, id: String): T? = findEntry(kClass) { it.id == id }

	internal fun getFact(id: String) = facts.firstOrNull { it.id == id }
	internal fun findFactByName(name: String) = facts.firstOrNull { it.name == name }
}

private fun JsonReader.parsePage(gson: Gson): Page =
	gson.fromJson(this, Page::class.java)

class Page(
	val entries: List<Entry>,
)

fun Iterable<Criteria>.matches(facts: Set<Fact>): Boolean = all { it.isValid(facts) }