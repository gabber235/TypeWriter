package me.ahdg6.typewriter.mythicmobs.entries.event

import io.lumine.mythic.api.mobs.MythicMob
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import java.util.*


@Entry("on_mythic_mob_die", "When a player kill a MythicMobs mob.", Colors.YELLOW, Icons.SKULL)
class MythicMobDeathEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<String> = emptyList(),
    @Help("Only trigger when a specific mob dies.")
    val mobName: String = "",
) : EventEntry

@EntryListener(MythicMobDeathEventEntry::class)
fun onMobDeath(event: MythicMobDeathEvent, query: Query<MythicMobDeathEventEntry>) {
    val player = event.killer as? Player ?: return
    query findWhere { it.mobName == event.mob.name } triggerAllFor player
}
