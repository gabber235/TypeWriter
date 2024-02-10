package me.ahdg6.typewriter.mythicmobs.entries.cinematic

import io.lumine.mythic.api.mobs.GenericCaster
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.skills.SkillMetadataImpl
import io.lumine.mythic.core.skills.SkillTriggers
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.InnerMax
import me.gabber235.typewriter.adapters.modifiers.Max
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.logger
import org.bukkit.entity.Player

@Entry("mythicskill_cinematic", "Spawn a MythicMob during a cinematic", Colors.PURPLE, "fa6-solid:bolt-lightning")
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
    @Help("The name of the skill to trigger")
    val skillName: String = "",
) : Segment

class SkillCinematicAction(
    private val player: Player,
    entry: MythicSkillCinematicEntry,
) : SimpleCinematicAction<MythicSkillSegment>() {
    override val segments: List<MythicSkillSegment> = entry.segments

    override suspend fun startSegment(segment: MythicSkillSegment) {
        super.startSegment(segment)

        val skill = MythicBukkit.inst().skillManager.getSkill(segment.skillName).orElseGet {
            throw IllegalArgumentException("Skill ${segment.skillName} not found")
        }

        val trigger = BukkitAdapter.adapt(player)
        val caster = GenericCaster(trigger)

        val skillMeta =
            SkillMetadataImpl(SkillTriggers.API, caster, trigger)

        if (skill.isUsable(skillMeta)) skill.execute(skillMeta)
        else logger.warning("Skill ${segment.skillName} is not usable at this time (cooldown, etc.)")
    }
}
