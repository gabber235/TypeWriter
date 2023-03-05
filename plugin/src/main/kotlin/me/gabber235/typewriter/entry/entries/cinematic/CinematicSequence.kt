package me.gabber235.typewriter.entry.entries.cinematic

import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.*
import me.gabber235.typewriter.entry.triggerAllFor
import org.bukkit.entity.Player
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

class CinematicSequence(private val player: Player) {
	private val actions = ConcurrentHashMap<String, CinematicAction>()
	private val revertibles = mutableListOf<Revertible>()

	fun tick(delta: Duration) {
		actions.values.forEach {
			try {
				it.tick(delta)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}

		val removing = actions.entries.filter { it.value.canFinish }.map { (id, action) ->
			action.teardown()?.let(revertibles::add)
			triggerNextEntriesFor(id)
			id
		}.toSet()
		actions.keys.removeAll(removing)
	}

	fun end() {
		revertibles.forEach { it.revert() }
	}

	fun add(entry: CinematicEntry) {
		if (actions.containsKey(entry.id)) return
		val action = entry.create(player)
		action.setup()
		actions[entry.id] = action
	}

	fun triggerNextEntriesFor(id: String) {
		val entry = Query.findById<CinematicEntry>(id) ?: return
		entry triggerAllFor player
	}
}