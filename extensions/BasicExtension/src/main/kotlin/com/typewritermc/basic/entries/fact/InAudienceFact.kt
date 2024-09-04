package com.typewritermc.basic.entries.fact

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entries.AudienceEntry
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.ReadableFactEntry
import com.typewritermc.engine.paper.entry.inAudience
import com.typewritermc.engine.paper.facts.FactData
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