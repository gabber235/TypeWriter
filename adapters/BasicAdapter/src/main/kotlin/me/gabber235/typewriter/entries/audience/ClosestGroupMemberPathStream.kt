package me.gabber235.typewriter.entries.audience

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceDisplay
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.RoadNetworkEntry
import me.gabber235.typewriter.entry.roadnetwork.gps.PathStreamDisplay

@Entry(
    "closest_group_member_path_stream",
    "A Path Stream to the Closest Group Member",
    Colors.GREEN,
    "material-symbols:conversion-path"
)
/**
 * The `Closest Group Member Path Stream` entry is a path stream that shows the path to the closest group member.
 * The 'Closest Group Member' is determined by the group member that is closest to the player geographically,
 * **Not based on the path distance.**
 *
 * When the group is not set, the path stream will not display anything.
 * Players must be in the same world for the path stream to consider them.
 *
 * ## How could this be used?
 * This could be used to show a path to the closest group member in a group of players.
 * When a player is lost, they can follow the path to the closest group member.
 */
class ClosestGroupMemberPathStream(
    override val id: String = "",
    override val name: String = "",
    val road: Ref<RoadNetworkEntry> = emptyRef(),
    val group: Ref<out GroupEntry> = emptyRef(),
) : AudienceEntry {
    override fun display(): AudienceDisplay = PathStreamDisplay(road) { player ->
        group.get()?.group(player)?.players
            ?.asSequence()
            ?.filter { it != player }
            ?.filter { player.world == it.world }
            ?.minByOrNull { it.location.distanceSquared(player.location) }
            ?.location
            ?: player.location
    }
}