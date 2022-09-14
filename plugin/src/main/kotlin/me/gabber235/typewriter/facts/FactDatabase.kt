package me.gabber235.typewriter.facts

import com.github.shynixn.mccoroutine.launchAsync
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.facts.storage.FileFactStorage
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque

object FactDatabase {
	private lateinit var storage: FactStorage

	// Local stored version of player facts
	private val cache = ConcurrentHashMap<UUID, Set<Fact>>()

	// Queue of players which facts need to be saved
	private val flush = ConcurrentLinkedDeque<UUID>()

	fun init() {
		storage = FileFactStorage()
		storage.init()

		plugin.listen<AsyncPlayerPreLoginEvent> { event ->
			runBlocking {
				loadFacts(event.uniqueId)
			}
		}

		plugin.listen<PlayerQuitEvent> { event ->
			val facts = cache.remove(event.player.uniqueId)
			if (facts != null) {
				plugin.launchAsync {
					storeFacts(event.player.uniqueId)
				}
			}
		}

		plugin.launchAsync {
			while (plugin.isEnabled) {
				delay(1000)
				cache.keys.forEach { uuid ->
					cache.computeIfPresent(uuid) { _, facts ->
						val newFacts = facts.filterExpiredFacts()
						if (newFacts.size != facts.size) flush.add(uuid)
						newFacts
					}
				}

				while (flush.isNotEmpty()) {
					storeFacts(flush.poll())
				}
			}
		}
	}

	fun shutdown() {
		runBlocking {
			cache.keys.forEach { uuid ->
				storeFacts(uuid)
			}
		}
		flush.clear()
		storage.shutdown()
	}

	private fun Set<Fact>.filterExpiredFacts(): Set<Fact> {
		return filter { !it.hasExpired }.toSet()
	}

	private fun Set<Fact>.filterSavableFacts(): Set<Fact> {
		val ignore = EntryDatabase.facts.filter { it.lifetime == FactLifetime.SESSION }.map { it.id }
		return filter { !ignore.contains(it.id) }.filter { it.value != 0 }.toSet()
	}

	private suspend fun loadFacts(uuid: UUID): Set<Fact> {
		val facts = storage.loadFacts(uuid)
			.filterExpiredFacts()
		cache[uuid] = facts
		return facts
	}

	private suspend fun storeFacts(uuid: UUID) {
		val facts = cache[uuid]?.filterExpiredFacts()?.filterSavableFacts() ?: return
		storage.storeFacts(uuid, facts)
	}

	fun getFacts(uuid: UUID): Set<Fact> = cache.getOrPut(uuid) { emptySet() }

	fun getFact(uuid: UUID, name: String): Fact {
		val facts = getFacts(uuid)
		val fact = facts.find { it.id == name }
		return if (fact != null) fact
		else {
			// We dont have to save this since it will not be saved when a fact.value == 0
			val newFact = Fact(name, 0)
			this.cache[uuid] = facts + newFact
			newFact
		}
	}

	fun getCachedFact(uuid: UUID, name: String) =
		cache[uuid]?.firstOrNull { it.id == name || EntryDatabase.getFact(it.id)?.name == name }

	fun modify(uuid: UUID, modifiers: List<Modifier>) {
		modify(uuid) {
			modifiers.forEach { modifier ->
				modify(modifier.fact) {
					when (modifier.operator) {
						ModifierOperator.ADD -> it + modifier.value
						ModifierOperator.SET -> modifier.value
					}
				}
			}
		}
	}

	fun modify(uuid: UUID, modifier: FactsModifier.() -> Unit) {
		cache.compute(uuid) { _, facts ->
			val factsModifier = FactsModifier(facts ?: emptySet())
			modifier(factsModifier)
			factsModifier.build()
		}
		flush.add(uuid)
	}
}

class FactsModifier(private val facts: Set<Fact>) {

	private val modifications = mutableMapOf<String, Int>()

	fun modify(id: String, modifier: (Int) -> Int) {
		modifications[id] = modifier(modifications[id] ?: facts.firstOrNull { it.id == id }?.value ?: 0)
	}

	fun set(id: String, value: Int) {
		modifications[id] = value
	}

	fun build(): Set<Fact> {
		val newFacts = facts.toMutableSet()
		modifications.forEach { (name, value) ->
			val fact = newFacts.find { it.id == name }
			if (fact != null) {
				newFacts.remove(fact)
				if (value != 0) newFacts.add(fact.copy(value = value, lastUpdate = LocalDateTime.now()))
			} else if (value != 0) {
				newFacts.add(Fact(name, value))
			}
		}
		return newFacts
	}
}

fun Player.fact(name: String) = FactDatabase.getFact(uniqueId, name)

val Player.facts: Set<Fact>
	get() = FactDatabase.getFacts(uniqueId)