package me.ahdg6.typewriter.mythicmobs.entries.event

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import org.bukkit.entity.Player


@Entry("on_mythic_mob_die", "When a player kill a MythicMobs mob.", Colors.YELLOW, "fa6-solid:skull")
/**
 * The `Mob Death Event` event is triggered when a player kill a mob.
 *
 * ## How could this be used?
 *
 * After killing a final boss, a dialogue or cinematic section can start. The player could also get a special reward the first time they kill a specific mob.
 */
class MythicMobDeathEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("Only trigger when a specific mob dies.")
    val mobName: String = "",
) : EventEntry

@EntryListener(MythicMobDeathEventEntry::class)
fun onMobDeath(event: MythicMobDeathEvent, query: Query<MythicMobDeathEventEntry>) {
    val player = event.killer as? Player ?: return
    query findWhere { it.mobName == event.mob.name } triggerAllFor player
}
