package me.ahdg6.typewriter.mythicmobs.entries.fact

import io.lumine.mythic.bukkit.MythicBukkit
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.FactData
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player

@Entry(
    "mythic_mob_count_fact",
    "Count the number of active Mythic Mobs of the specified type",
    Colors.PURPLE,
    Icons.PLACE_OF_WORSHIP
)
/**
 * A [fact](/docs/facts) that represents how many specific MythicMobs mob are in the world.
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
    override val audience: Ref<AudienceEntry> = emptyRef(),
    @Help("The id of the mob to count")
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