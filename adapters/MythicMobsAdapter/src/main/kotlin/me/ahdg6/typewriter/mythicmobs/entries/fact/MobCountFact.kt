package me.ahdg6.typewriter.mythicmobs.entries.fact

import io.lumine.mythic.bukkit.MythicBukkit
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Regex
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.facts.FactData
import org.bukkit.entity.Player
import java.util.regex.Pattern
import java.util.stream.Collectors

@Entry(
    "mythic_mob_count_fact",
    "Count the number of active Mythic Mobs of the specified type",
    Colors.PURPLE,
    "mingcute:counter-fill"
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
    override val group: Ref<GroupEntry> = emptyRef(),
    @Help("The id of the mob to count")
    @Regex
    val mobName: String = "",
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val regex = mobName.toRegex(RegexOption.IGNORE_CASE)
        val mobs = MythicBukkit.inst().mobManager.mobTypes
            .filter { regex.matches(it.internalName) }
        if (mobs.isEmpty()) return FactData(0)

        var count = 0
        for (activeMob in MythicBukkit.inst().mobManager.activeMobs) {
            if (mobs.contains(activeMob.type)) {
                count++
            }
        }

        return FactData(count)
    }
}