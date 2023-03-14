package com.caleb.typewriter.mythicmobs.entries.action

import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*


@Entry("spawn_mythicmobs_mob", "Spawn a mob from mythicmobs", Colors.ORANGE, Icons.HANDSHAKE)
class SpawnMobActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    @Help("The mob's name")
    private val mob: String = "",
    @Help("The mob's level")
    private val level: Double = 1.0,
    @Help("Use player's location")
    private val usePlayerLocation: Boolean = true,
    @Help("SpawnLocation")
    private var spawnLocation: Location,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        if (usePlayerLocation) spawnLocation = player.getLocation().add(location)

        val mob = MythicBukkit.inst().mobManager.getMythicMob(mob) ?: return
        mob.get().spawn(BukkitAdapter.adapt(spawnLocation), level)
    }
}