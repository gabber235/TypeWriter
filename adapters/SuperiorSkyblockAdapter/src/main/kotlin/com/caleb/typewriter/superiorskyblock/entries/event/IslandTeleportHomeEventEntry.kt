package com.caleb.typewriter.superiorskyblock.entries.event

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import java.util.*

@Entry("on_teleport_skyblock_home", "[SuperiorSkyblock] When a player teleports to Skyblock home", Colors.YELLOW, Icons.GLOBE)
class IslandTeleportHomeEventEntry (
	override val id: String = "",
	override val name: String = "",
	override val triggers: List<String> = emptyList(),
) : EventEntry

@EntryListener(IslandTeleportHomeEventEntry::class)
fun onHomeCommand(event: PlayerCommandPreprocessEvent, query: Query<IslandTeleportHomeEventEntry>) {

	var player: Player = event.player ?: return
	var sPlayer: SuperiorPlayer = SuperiorSkyblockAPI.getPlayer(player)

	if(sPlayer.island == null) return

	if(event.message != "/is home" && event.message != "/island home") return

	query.find() triggerAllFor player
}