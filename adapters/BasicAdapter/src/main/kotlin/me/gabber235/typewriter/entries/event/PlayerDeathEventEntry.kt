package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

@Entry("on_player_death", "When a player dies", Colors.YELLOW, "fa6-solid:skull-crossbones")
/**
 * The `Player Death Event` is fired when any player dies. This event allows you to select the cause of death if you wish. If you want to detect when another player kills a player, use the [`Player Kill Player Event`](on_player_kill_player).
 *
 * ## How could this be used?
 *
 * You can create custom death messages for certain types of deaths, such as falling, drowning, or being killed by another player.
 */
class PlayerDeathEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The cause of the death.")
    // If specified, the death cause must match the cause of death in order for the event to trigger.
    val deathCause: Optional<DamageCause> = Optional.empty()
) : EventEntry


@EntryListener(PlayerDeathEventEntry::class)
fun onDeath(event: EntityDeathEvent, query: Query<PlayerDeathEventEntry>) {
    if (event.entity !is Player) return

    val player = event.entity as Player

    query findWhere { entry ->
        entry.deathCause.map { it == event.entity.lastDamageCause?.cause }.orElse(true)
    } triggerAllFor player
}