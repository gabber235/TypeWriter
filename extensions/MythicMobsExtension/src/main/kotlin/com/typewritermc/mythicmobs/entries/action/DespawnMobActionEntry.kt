package com.typewritermc.mythicmobs.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Placeholder
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.extensions.placeholderapi.parsePlaceholders
import com.typewritermc.engine.paper.utils.ThreadType.SYNC
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.entity.Player


@Entry("despawn_mythicmobs_mob", "Despawn a mob from MythicMobs", Colors.ORANGE, "fluent:crown-subtract-24-filled")
/**
 * The `Despawn Mob Action` action removes MythicMobs mobs from the world.
 *
 * ## How could this be used?
 *
 * This action could be used in stories or quests in various ways. For instance, if a player fails a quest to kill 10 zombies, then the zombies could be despawned to signal that the quest is no longer active. One could even use this action for a quest to kill a certain amount of mobs within a time limit!
 */
class DespawnMobActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Placeholder
    private val mobName: String = "",
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val mob = MythicBukkit.inst().mobManager.getMythicMob(mobName.parsePlaceholders(player))
        if (!mob.isPresent) return

        SYNC.launch {
            MythicBukkit.inst().mobManager.activeMobs.removeIf {
                if (it.type == mob.get()) {
                    it.entity.remove()
                    return@removeIf true
                }
                false
            }
        }
    }
}