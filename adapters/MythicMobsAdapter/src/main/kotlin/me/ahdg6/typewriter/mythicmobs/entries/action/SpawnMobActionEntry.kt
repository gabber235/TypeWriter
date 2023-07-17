package me.ahdg6.typewriter.mythicmobs.entries.action

import com.github.shynixn.mccoroutine.bukkit.launch
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.entity.Player


@Entry("spawn_mythicmobs_mob", "Spawn a mob from MythicMobs", Colors.ORANGE, Icons.DRAGON)
/**
 * The `Spawn Mob Action` action spawn MythicMobs mobs to the world.
 *
 * ## How could this be used?
 *
 * This action could be used in a plethora of scenarios. From simple quests requiring you to kill some spawned mobs, to complex storylines that simulate entire battles, this action knows no bounds!
 */
class SpawnMobActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    @Help("The mob's name")
    private val mobName: String = "",
    @Help("The mob's level")
    private val level: Double = 1.0,
    @Help("The mob's spawn location")
    private var spawnLocation: Location,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val mob = MythicBukkit.inst().mobManager.getMythicMob(mobName)
        if (!mob.isPresent) return

        plugin.launch {
            mob.get().spawn(BukkitAdapter.adapt(spawnLocation), level)
        }
    }
}