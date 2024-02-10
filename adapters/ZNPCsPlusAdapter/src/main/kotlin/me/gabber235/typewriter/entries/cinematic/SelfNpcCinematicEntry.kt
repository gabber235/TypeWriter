package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.NpcCinematicAction
import me.gabber235.typewriter.entry.entries.NpcCinematicEntry
import me.gabber235.typewriter.entry.entries.NpcRecordedSegment
import org.bukkit.entity.Player

@Entry("znpc_self_npc_cinematic", "The player itself as an cinematic npc", Colors.PINK, "heroicons:user-16-solid")
/**
 * The `Self NPC Cinematic` entry that plays a recorded animation back on the player with an NPC with the player's skin.
 * If the NPC recording does not have any armor, the player's armor when starting the cinematic will be used.
 *
 * ## How could this be used?
 *
 * This could be used to create a cinematic where the player is talking to an NPC.
 * Like going in to a store and talking to the shopkeeper.
 */
class SelfNpcCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val recordedSegments: List<NpcRecordedSegment> = emptyList(),
) : NpcCinematicEntry {
    override fun create(player: Player): CinematicAction {
        return NpcCinematicAction(
            player,
            this,
            PlayerNpcData(),
        )
    }
}