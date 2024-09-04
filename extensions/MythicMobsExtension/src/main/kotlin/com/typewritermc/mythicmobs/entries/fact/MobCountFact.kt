package com.typewritermc.mythicmobs.entries.fact

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.facts.FactData
import io.lumine.mythic.bukkit.MythicBukkit
import org.bukkit.entity.Player

@Entry(
    "mythic_mob_count_fact",
    "Count the number of active Mythic Mobs of the specified type",
    Colors.PURPLE,
    "mingcute:counter-fill"
)
/**
 * A [fact](/docs/creating-stories/facts) that represents how many specific MythicMobs mob are in the world.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 *
 * This fact could be used to change dialogue sent by an NPC or mob when a boss exists. It could also be used in conjunction with the Spawn Mob action to spawn specific mobs if one or more mobs exist/doesn't exist.
 */
class MobCountFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    val mobName: String = "",
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val mob = MythicBukkit.inst().mobManager.getMythicMob(mobName)
        if (!mob.isPresent) return FactData(0)

        var count = 0
        for (activeMob in MythicBukkit.inst().mobManager.activeMobs) {
            if (activeMob.type == mob.get()) {
                count++
            }
        }

        return FactData(count)
    }
}