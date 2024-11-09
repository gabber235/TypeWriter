package com.typewritermc.basic.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.EventEntry
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.*

@Entry("on_player_hit_entity", "When a player hits an entity", Colors.YELLOW, "fa6-solid:heart-crack")
/**
 * The `Player Hit Entity Event` event is fired when a player hits an entity. If you want to detect when a player kills an entity, use the [`Player Kill Entity Event`](on_player_kill_entity) event.
 *
 * ## How could this be used?
 *
 * This event could be used to create a custom game mode where players have to hit a certain number of entities to win. It could also be used to detect when you hit a certain entity, and make it be aggresive towards you.
 */
class PlayerHitEntityEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    val entityType: Optional<EntityType> = Optional.empty(),
) : EventEntry


@EntryListener(PlayerHitEntityEventEntry::class)
fun onHit(event: EntityDamageByEntityEvent, query: Query<PlayerHitEntityEventEntry>) {
    if (event.damager !is Player) return

    val player = event.damager as Player

    query findWhere { entry ->
        entry.entityType.map { it == event.entityType }.orElse(true)
    } triggerAllFor player
}