package me.gabber235.typewriter.entry

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.adapters.AdapterEntryType
import me.gabber235.typewriter.adapters.AdapterLoader
import me.gabber235.typewriter.entry.action.ActionEntry
import me.gabber235.typewriter.entry.action.SimpleActionEntry
import me.gabber235.typewriter.entry.dialogue.DialogueEntry
import me.gabber235.typewriter.entry.event.EventEntry
import me.gabber235.typewriter.entry.event.entries.IslandCreateEventEntry
import me.gabber235.typewriter.entry.event.entries.NpcEventEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.facts.FactEntry
import me.gabber235.typewriter.utils.RuntimeTypeAdapterFactory
import me.gabber235.typewriter.utils.get
import kotlin.reflect.KClass

object EntryDatabase {
	var facts = listOf<FactEntry>()
		private set
	private var speakers = listOf<SpeakerEntry>()
	private var events = listOf<EventEntry>()
	private var dialogue = listOf<DialogueEntry>()
	private var actions = listOf<ActionEntry>()

	fun loadEntries() {
		val dir = Typewriter.plugin.dataFolder["pages"]
		if (!dir.exists()) {
			dir.mkdirs()
		}

		val gson = gson()

		val pages = dir.listFiles { file -> file.name.endsWith(".json") }?.map { file ->
			val dialogueReader = JsonReader(file.reader())
			dialogueReader.parsePage(gson)
		}

		this.facts = pages?.flatMap { it.facts } ?: listOf()
		this.speakers = pages?.flatMap { it.speakers } ?: listOf()
		this.events = pages?.flatMap { it.events } ?: listOf()
		this.dialogue = pages?.flatMap { it.dialogue } ?: listOf()
		this.actions = pages?.flatMap { it.actions } ?: listOf()

		println("Loaded ${facts.size} fact, ${speakers.size} speaker, ${events.size} event, ${dialogue.size} dialogue and ${actions.size} action entries")
	}

	fun gson(): Gson {
		val actionFactory = RuntimeTypeAdapterFactory.of(ActionEntry::class.java)
			.registerSubtype(SimpleActionEntry::class.java, "simple")

		val dialogueFactory = RuntimeTypeAdapterFactory.of(DialogueEntry::class.java)

		val eventFactory = RuntimeTypeAdapterFactory.of(EventEntry::class.java)
			.registerSubtype(NpcEventEntry::class.java, "npc_interact")
			.registerSubtype(IslandCreateEventEntry::class.java, "island_create")

		AdapterLoader.getAdapterData().flatMap { it.entries }.forEach {
			println("Registering ${it.type} entry ${it.name}")
			when (it.type) {
				AdapterEntryType.ACTION   -> actionFactory.registerSubtype(it.clazz as Class<out ActionEntry>, it.name)
				AdapterEntryType.DIALOGUE -> dialogueFactory.registerSubtype(
					it.clazz as Class<out DialogueEntry>,
					it.name
				)

				AdapterEntryType.EVENT    -> eventFactory.registerSubtype(it.clazz as Class<out EventEntry>, it.name)
			}
		}

		return GsonBuilder()
			.registerTypeAdapterFactory(eventFactory)
			.registerTypeAdapterFactory(dialogueFactory)
			.registerTypeAdapterFactory(actionFactory)
			.create()
	}

	fun <T : EventEntry> findEventEntries(klass: KClass<T>, predicate: (T) -> Boolean): List<T> {
		return events.filterIsInstance(klass.java).filter(predicate)
	}

	fun findDialogue(trigger: String, facts: Set<Fact>): DialogueEntry? {
		val rules = dialogue.filter { trigger in it.triggerdBy }.sortedByDescending { it.criteria.size }
		return rules.find { rule -> rule.criteria.matches(facts) }
	}

	fun findActions(trigger: String, facts: Set<Fact>): List<ActionEntry> {
		return actions.filter { trigger in it.triggerdBy }.filter { rule -> rule.criteria.matches(facts) }
	}

	fun getSpeaker(id: String) = speakers.firstOrNull { it.id == id }
	fun findSpeakerByName(name: String) = speakers.firstOrNull { it.name == name }

	fun getFact(id: String) = facts.firstOrNull { it.id == id }
	fun findFactByName(name: String) = facts.firstOrNull { it.name == name }

	fun getAllTriggers() = listOf(
		*events.toTypedArray(),
		*dialogue.toTypedArray(),
		*actions.toTypedArray()
	).flatMap { entry ->
		val triggers = entry.triggers.toMutableList()
		if (entry is RuleEntry) {
			triggers += entry.triggerdBy
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

private class Page(
	val facts: List<FactEntry> = listOf(),
	val speakers: List<SpeakerEntry> = listOf(),
	val events: List<EventEntry> = listOf(),
	val dialogue: List<DialogueEntry> = listOf(),
	val actions: List<ActionEntry> = listOf(),
)

fun Iterable<Criteria>.matches(facts: Set<Fact>): Boolean = all { it.isValid(facts) }