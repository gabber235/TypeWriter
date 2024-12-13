package com.typewritermc.mythicmobs.entries.event

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Query
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.EntryListener
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.core.extension.annotations.Regex
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.EventEntry
import com.typewritermc.engine.paper.entry.triggerAllFor
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.event.entity.PlayerDeathEvent
import java.util.*


@Entry("mythicmobs_kill_player_event", "MythicMob Kill Player Event", Colors.YELLOW, "fa6-solid:skull")
/**
 * The `MythicMob Kill Player Event` event is triggered when MythicMob kills a player.
 *
 * ## How could this be used?
 * When the player is killed by a certain monster, there is a probability that the monster will drag them back to their lair.
 */
class MythicMobKillPlayerEventEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The type of the MythicMob that killed the player")
    @Placeholder
    @Regex
    val mythicMobType: Optional<String> = Optional.empty(),
) : EventEntry

@EntryListener(MythicMobKillPlayerEventEntry::class)
fun onMythicMobKillPlayer(event: PlayerDeathEvent, query: Query<MythicMobKillPlayerEventEntry>) {
    val causingEntity = event.damageSource.causingEntity ?: return
    val mythicMob = MythicBukkit.inst().mobManager.getMythicMobInstance(causingEntity) ?: return

    query.findWhere { entry ->
        entry.mythicMobType.map { it.toRegex().matches(mythicMob.mobType) }.orElse(true)
    }.triggerAllFor(event.player, context())
}