package me.gabber235.typewriter.citizens.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.entity.Player

@Entry("self_npc_cinematic", "The player itself as an cinematic npc", Colors.PINK, Icons.USER)
data class SelfNpcCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val startLocation: Location = Location(null, 0.0, 0.0, 0.0, 0.0f, 0.0f),
    override val movementSegments: List<NpcMovementSegment> = emptyList(),
) : NpcCinematicEntry {
    override fun create(player: Player): CinematicAction {
        return NpcCinematicAction(
            player,
            this,
            PlayerNpcData(player),
        )
    }
}
