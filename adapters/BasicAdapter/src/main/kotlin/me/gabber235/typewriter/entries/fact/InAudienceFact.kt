package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.AudienceEntry
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.ReadableFactEntry
import me.gabber235.typewriter.entry.inAudience
import me.gabber235.typewriter.facts.FactData
import org.bukkit.entity.Player

@Entry(
    "in_audience_fact",
    "The fact that the player is in the audience",
    Colors.PURPLE,
    "material-symbols:person-pin"
)
/**
 * The `In Audience Fact` is a fact that the value specified if the player is in the audience.
 *
 * <fields.ReadonlyFactInfo />
 *
 * ## How could this be used?
 * This can be used to filter players if they are in a specific audience.
 */
class InAudienceFact(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
    val entries: Map<Ref<AudienceEntry>, Int> = emptyMap()
) : ReadableFactEntry {
    override fun readSinglePlayer(player: Player): FactData {
        val value = entries.firstNotNullOfOrNull { (ref, value) ->
            if (player.inAudience(ref)) value else null
        } ?: 0

        return FactData(value)
    }
}