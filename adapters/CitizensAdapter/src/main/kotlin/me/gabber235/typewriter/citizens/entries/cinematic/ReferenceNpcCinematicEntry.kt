package me.gabber235.typewriter.citizens.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.citizens.entries.entity.ReferenceNpcEntry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.utils.Icons
import org.bukkit.Location
import org.bukkit.entity.Player

@Entry(
    "reference_npc_cinematic",
    "A reference to an existing npc specifically for cinematic",
    Colors.PINK,
    Icons.USER_TIE
)
data class ReferenceNpcCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),

    override val startLocation: Location = Location(null, 0.0, 0.0, 0.0, 0.0f, 0.0f),
    override val movementSegments: List<NpcMovementSegment> = emptyList(),
    @Help("Reference npc to clone")
    @EntryIdentifier(ReferenceNpcEntry::class)
    val referenceNpc: String = "",
) : NpcCinematicEntry {
    override fun create(player: Player): CinematicAction {
        val referenceNpc =
            Query.findById<ReferenceNpcEntry>(this.referenceNpc) ?: throw Exception("Reference npc not found")

        return NpcCinematicAction(
            player,
            this,
            ReferenceNpcData(referenceNpc.npcId),
        )
    }
}
