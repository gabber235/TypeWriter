package me.gabber235.typewriter.entries.fact

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.emptyRef
import me.gabber235.typewriter.entry.entries.CachableFactEntry
import me.gabber235.typewriter.entry.entries.GroupEntry

@Entry("session_fact", "Saved until a player logouts of the server", Colors.PURPLE, "fa6-solid:user-clock")
/**
 * This [fact](/docs/facts) is stored until the player logs out.
 *
 * ## How could this be used?
 *
 * This could be used to slowly add up a player's total time played, and reward them with a badge or other reward when they reach a certain amount of time.
 */
class SessionFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
) : CachableFactEntry