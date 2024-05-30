package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDeathEvent
import java.util.*

@Entry("on_player_kill_entity", "When a player kills an entity", Colors.YELLOW, "fa6-solid:skull")
/**
 * The `Player Kill Entity Event` is fired when a player kills an entity. If you want to detect when a player kills another player, use the [`Player Kill Player Event`](on_player_kill_player) instead.
 *
 * ## How could this be used?
 *
 * This event could be used to detect when a player kills a boss, and give them some rewards. It could also be used to create a custom mob that drops items when killed by a player.
 */
class PlayerKillEntityEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The type of entity that was killed.")
    // If specified, the entity type must match the entity type that was killed in order for the event to trigger.
    val entityType: Optional<EntityType> = Optional.empty(),
) : EventEntry


@EntryListener(PlayerKillEntityEventEntry::class)
fun onKill(event: EntityDeathEvent, query: Query<PlayerKillEntityEventEntry>) {
    val killer = event.entity.killer ?: return

    query findWhere { entry ->
        entry.entityType.map { it == event.entityType }.orElse(true)
    } triggerAllFor killer
}