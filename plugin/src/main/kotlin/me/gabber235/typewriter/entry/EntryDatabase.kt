package me.gabber235.typewriter.entry

import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import me.gabber235.typewriter.Typewriter
import me.gabber235.typewriter.entry.dialogue.DialogueEntry
import me.gabber235.typewriter.entry.dialogue.entries.OptionDialogueEntry
import me.gabber235.typewriter.entry.dialogue.entries.SpokenDialogueEntry
import me.gabber235.typewriter.entry.event.EventEntry
import me.gabber235.typewriter.entry.event.entries.NpcEventEntry
import me.gabber235.typewriter.facts.Fact
import me.gabber235.typewriter.facts.FactEntry
import me.gabber235.typewriter.utils.RuntimeTypeAdapterFactory
import java.io.File
import kotlin.reflect.KClass

object EntryDatabase {
	private var facts = listOf<FactEntry>()
	private var events = listOf<EventEntry>()
	private var dialogue = listOf<DialogueEntry>()

	fun loadEntries() {
		val dir = Typewriter.plugin.dataFolder["pages"]
		if (!dir.exists()) {
			dir.mkdirs()
		}

		val pages = dir.listFiles { file -> file.name.endsWith(".json") }?.map { file ->
			val dialogueReader = JsonReader(file.reader())
			dialogueReader.parsePage()
		}

		this.facts = pages?.flatMap { it.facts } ?: listOf()
		this.events = pages?.flatMap { it.events } ?: listOf()
		this.dialogue = pages?.flatMap { it.dialogue } ?: listOf()

		println("Loaded ${facts.size} facts, ${events.size} events and ${dialogue.size} dialogue entries")
	}

	fun <T : EventEntry> findEventEntries(klass: KClass<T>, predicate: (T) -> Boolean): List<T> {
		return events.filterIsInstance(klass.java).filter(predicate)
	}

	fun findDialogue(trigger: String, facts: Set<Fact>): DialogueEntry? {
		val rules = dialogue.filter { trigger in it.triggerdBy }.sortedByDescending { it.criteria.size }
		return rules.find { rule -> rule.criteria.matches(facts) }
	}
}

private val eventFactory = RuntimeTypeAdapterFactory.of(EventEntry::class.java)
	.registerSubtype(NpcEventEntry::class.java, "npc_interact")

private val dialogueFactory = RuntimeTypeAdapterFactory.of(DialogueEntry::class.java)
	.registerSubtype(SpokenDialogueEntry::class.java, "spoken")
	.registerSubtype(OptionDialogueEntry::class.java, "option")

private val gson = GsonBuilder()
	.registerTypeAdapterFactory(eventFactory)
	.registerTypeAdapterFactory(dialogueFactory)
	.create()

private fun JsonReader.parsePage(): Page =
	gson.fromJson(this, Page::class.java)

private class Page(
	val facts: List<FactEntry>,
	val events: List<EventEntry>,
	val dialogue: List<DialogueEntry>
)

private operator fun File.get(name: String): File = File(this, name)

fun Iterable<Criteria>.matches(facts: Set<Fact>): Boolean = all { it.isValid(facts) }