package me.gabber235.typewriter.facts

import lirand.api.extensions.events.listen
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.ModifierOperator
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object FactDatabase {
	private val facts = ConcurrentHashMap<UUID, Set<Fact>>()

	fun init() {
		plugin.listen<PlayerQuitEvent> { event ->
			facts.remove(event.player.uniqueId)
		}
	}

	fun getFacts(uuid: UUID): Set<Fact> = facts.getOrPut(uuid) { emptySet() }
	fun getFact(uuid: UUID, name: String): Fact {
		val facts = getFacts(uuid)
		val fact = facts.find { it.id == name }
		return if (fact != null) fact
		else {
			val newFact = Fact(name, 0)
			this.facts[uuid] = facts + newFact
			newFact
		}
	}

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
		facts.compute(uuid) { _, facts ->
			val factsModifier = FactsModifier(facts ?: emptySet())
			modifier(factsModifier)
			factsModifier.build()
		}
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