package me.ahdg6.typewriter.mythicmobs.entries.action

import io.lumine.mythic.api.mobs.GenericCaster
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.skills.SkillMetadataImpl
import io.lumine.mythic.core.skills.SkillTriggers
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.logger
import org.bukkit.entity.Player

@Entry("execute_mythicmob_skill", "Executes a MythicMobs skill", Colors.RED, "fa6-solid:bolt-lightning")
/**
 * The `Execute Skill Action` action executes a MythicMobs skill.
 *
 * ## How could this be used?
 * Create fancy particle animations.
 * For example, you can create a little animation when a player opens a door.
 */
class ExecuteSkillActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    @Help("The name of the skill to execute")
    val skillName: String = "",
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val skill = MythicBukkit.inst().skillManager.getSkill(skillName).orElseGet {
            throw IllegalArgumentException("Skill $skillName not found")
        }
        val trigger = BukkitAdapter.adapt(player)
        val caster = GenericCaster(trigger)

        val skillMeta =
            SkillMetadataImpl(SkillTriggers.API, caster, trigger)

        if (skill.isUsable(skillMeta)) skill.execute(skillMeta)
        else logger.warning("Skill $skillName is not usable at this time (cooldown, etc.)")
    }
}