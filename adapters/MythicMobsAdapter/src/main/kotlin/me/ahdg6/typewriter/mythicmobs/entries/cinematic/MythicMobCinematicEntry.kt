package me.ahdg6.typewriter.mythicmobs.entries.cinematic

import io.lumine.mythic.api.mobs.GenericCaster
import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.core.mobs.ActiveMob
import io.lumine.mythic.core.skills.SkillMetadataImpl
import io.lumine.mythic.core.skills.SkillTriggers
import lirand.api.extensions.server.server
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.utils.ThreadType.SYNC
import org.bukkit.Location
import org.bukkit.entity.Player

@Entry("mythicmob_cinematic", "Spawn a MythicMob during a cinematic", Colors.PURPLE, "fa6-solid:dragon")
/**
 * The `Spawn MythicMob Cinematic` cinematic entry spawns a MythicMob during a cinematic.
 *
 * ## How could this be used?
 *
 * This can be used to animate a MythicMob spawning during a cinematic.
 */
class MythicMobCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(Colors.PURPLE, "fa6-solid:dragon")
    val segments: List<MythicMobSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return MobCinematicAction(player, this)
    }
}

data class MythicMobSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    @Help("The name of the mob to spawn")
    val mobName: String = "",
    @Help("The location to spawn the mob at")
    val location: Location = Location(null, 0.0, 0.0, 0.0),
) : Segment

class MobCinematicAction(
    private val player: Player,
    entry: MythicMobCinematicEntry,
) : SimpleCinematicAction<MythicMobSegment>() {
    override val segments: List<MythicMobSegment> = entry.segments

    private var mob: ActiveMob? = null

    override suspend fun startSegment(segment: MythicMobSegment) {
        super.startSegment(segment)

        SYNC.switchContext {
            val mob = MythicBukkit.inst().mobManager.spawnMob(segment.mobName, segment.location)
            this@MobCinematicAction.mob = mob
            val hideMechanic = MythicBukkit.inst().skillManager.getMechanic("hide")

            val targets =
                server.onlinePlayers.filter { it.uniqueId != player.uniqueId }.map { BukkitAdapter.adapt(it) }.toSet()

            val skillMeta = SkillMetadataImpl(
                SkillTriggers.API,
                GenericCaster(mob.entity),
                mob.entity,
                mob.location,
                targets,
                null,
                1f
            )

            hideMechanic.execute(skillMeta)
        }
    }

    override suspend fun stopSegment(segment: MythicMobSegment) {
        super.stopSegment(segment)

        mob?.let {
            it.despawn()
            mob = null
        }
    }
}