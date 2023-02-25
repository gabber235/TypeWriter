package com.caleb.typewriter.worldguard.entries.events

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*

@Entry("on_leave_region", "[WorldGuard] When a player leaves a region", Colors.YELLOW, Icons.SQUARE_XMARK)
class LeaveRegionEventEntry (
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
	@Help("The region to check for.")
	val region: String = "",
) : EventEntry

@EntryListener(LeaveRegionEventEntry::class)
fun onMove(event: PlayerMoveEvent, query: Query<LeaveRegionEventEntry>) {
	val player: Player = event.player

	val regionContainer = WorldGuard.getInstance().platform.regionContainer
	val regionManager = regionContainer.get(BukkitAdapter.adapt(player.world))
	val regionsTo = regionManager?.getApplicableRegions(BukkitAdapter.asBlockVector(event.from)) ?: return
	val regionsFrom = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(event.to)) ?: return

	//region is in regionsFrom but not in regionsTo
	(query findWhere { it.region in regionsFrom.map { it.id } && it.region !in regionsTo.map { it.id } }) triggerAllFor player
}

