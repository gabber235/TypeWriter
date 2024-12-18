package com.typewritermc.mythicmobs.entries.cinematic

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.InnerMax
import com.typewritermc.core.extension.annotations.Max
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.entry.temporal.SimpleCinematicAction
import com.typewritermc.engine.paper.logger
import io.lumine.mythic.api.mobs.GenericCaster
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.skills.SkillMetadataImpl
import io.lumine.mythic.core.skills.SkillTriggers
import org.bukkit.entity.Player

@Entry("mythicskill_cinematic", "Trigger a MythicSkill during a cinematic", Colors.PURPLE, "fa6-solid:bolt-lightning")
/**
 * The `Mythic Skill Cinematic` cinematic entry triggers a skill during a cinematic.
 *
 * :::caution
 * A skill itself needs to define the group to be self using `group=self`
 *
 * ### Example:
 * ```
 * - effect:particlesphere{particle=flame;amount=200;radius=2;group=self} @self
 * ```
 * :::
 *
 * ## How could this be used?
 *
 * This can be used to animate fancy particle effects during a cinematic.
 */
class MythicSkillCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(Colors.PURPLE, "fa6-solid:bolt-lightning")
    @InnerMax(Max(1))
    val segments: List<MythicSkillSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return SkillCinematicAction(player, this)
    }
}

data class MythicSkillSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    val skillName: Var<String> = ConstVar(""),
) : Segment

class SkillCinematicAction(
    private val player: Player,
    entry: MythicSkillCinematicEntry,
) : SimpleCinematicAction<MythicSkillSegment>() {
    override val segments: List<MythicSkillSegment> = entry.segments

    override suspend fun startSegment(segment: MythicSkillSegment) {
        super.startSegment(segment)

        val skillName = segment.skillName.get(player)
        val skill = MythicBukkit.inst().skillManager.getSkill(skillName).orElseGet {
            throw IllegalArgumentException("Skill $skillName not found")
        }

        val trigger = BukkitAdapter.adapt(player)
        val caster = GenericCaster(trigger)

        val skillMeta =
            SkillMetadataImpl(SkillTriggers.API, caster, trigger)

        if (skill.isUsable(skillMeta)) skill.execute(skillMeta)
        else logger.warning("Skill ${segment.skillName} is not usable at this time (cooldown, etc.)")
    }
}
