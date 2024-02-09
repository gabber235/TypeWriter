package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.GroupEntry
import me.gabber235.typewriter.entry.entries.PersistableFactEntry
import me.gabber235.typewriter.utils.Icons

@Entry("permanent_fact", "Saved permanently, it never gets removed", Colors.PURPLE, Icons.DATABASE)
/**
 * This [fact](/docs/facts) is permanent and never expires.
 *
 * ## How could this be used?
 *
 * This could be used to store if a player as joined the server before. If the player has completed a quest. Maybe if the player has received a reward already to prevent them from receiving it again.
 */
class PermanentFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val audience: Ref<GroupEntry> = emptyRef(),
) : PersistableFactEntry
