package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.MaterialProperties
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.BLOCK
import me.gabber235.typewriter.adapters.modifiers.MaterialProperty.ITEM
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.entry.entries.SystemTrigger
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*

@Entry("on_player_move", "When the player moves", Colors.YELLOW, Icons.PERSON_WALKING)
class PlayerMoveEventEntry (
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	val location: Optional<Location> = Optional.empty(),
	val distance: Optional<Double> = Optional.empty(),
	val fullBlock: Boolean = false,
) : EventEntry

@EntryListener(PlayerMoveEventEntry::class)
fun onPlayerMove(event: PlayerMoveEvent, query: Query<PlayerMoveEventEntry>) {

	query.findWhere { entry ->

		// Check if the player moved to a new block
		if(entry.fullBlock && event.to.blockX == event.from.blockX && event.to.blockY == event.from.blockY && event.to.blockZ == event.from.blockZ) return@findWhere false

		if(entry.location.isPresent) {
			if (!entry.location.map { it == event.to }.orElse(true)) return@findWhere false
			if(entry.distance.isPresent) {
				if(entry.distance.get() >= event.to.distance(event.from)) return@findWhere false
			}
		}

		return@findWhere true
	}.startInteractionWithOrTrigger(event.player, SystemTrigger.DIALOGUE_NEXT)
}