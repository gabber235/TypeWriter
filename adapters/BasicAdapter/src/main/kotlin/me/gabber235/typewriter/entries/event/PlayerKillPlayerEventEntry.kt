package me.gabber235.typewriter.entries.event

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.EventEntry
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDeathEvent

@Entry("on_player_kill_player", "When a player kills a player", Colors.YELLOW, "fa6-solid:skull")
/**
 * The `Player Kill Player Event` is triggered when a player kills another player. If you want to detect when a player kills some thing else, use the [`Player Kill Entity Event`](on_player_kill_entity) instead.
 *
 * ## How could this be used?
 *
 * This could be used to create a bounty system, where someone places a bounty on another player, and when that player is killed, the bounty is paid out to the player who kills them.
 */
class PlayerKillPlayerEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The triggers to be executed for the player who was killed.")
    // The entries connected tho this field will be triggered for the player who was killed.
    val killedTriggers: List<Ref<TriggerableEntry>> = emptyList(),
) : EventEntry


@EntryListener(PlayerKillPlayerEventEntry::class)
fun onKill(event: EntityDeathEvent, query: Query<PlayerKillPlayerEventEntry>) {
    val killer =
        event.entity.killer ?: return

    val target = event.entity as? Player ?: return

    val entries = query.find()

    entries triggerAllFor killer
    entries.flatMap { it.killedTriggers } triggerEntriesFor target
}