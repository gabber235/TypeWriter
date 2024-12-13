package com.typewritermc.mythicmobs.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.entry.entries.ActionTrigger
import com.typewritermc.engine.paper.entry.entries.ConstVar
import com.typewritermc.engine.paper.entry.entries.Var
import com.typewritermc.engine.paper.logger
import io.lumine.mythic.api.mobs.GenericCaster
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.skills.SkillMetadataImpl
import io.lumine.mythic.core.skills.SkillTriggers
import io.lumine.mythic.core.utils.MythicUtil
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
    val skillName: Var<String> = ConstVar(""),
) : ActionEntry {
    override fun ActionTrigger.execute() {
        val skillName = skillName.get(player, context)
        val skill = MythicBukkit.inst().skillManager.getSkill(skillName).orElseGet {
            throw IllegalArgumentException("Skill $skillName not found")
        }
        val trigger = BukkitAdapter.adapt(player)
        val caster = GenericCaster(trigger)

        val skillMeta =
            SkillMetadataImpl(SkillTriggers.API, caster, trigger)

        if (skill.isUsable(skillMeta)) {
            MythicBukkit.inst().apiHelper.castSkill(
                player,
                skillName,
                player,
                player.location,
                listOf(MythicUtil.getTargetedEntity(player)),
                null,
                1f
            )
        } else logger.warning("Skill $skillName is not usable at this time (cooldown, etc.)")
    }
}