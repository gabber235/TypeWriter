package com.typewritermc.basic.entries.fact

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.entries.Ref
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.engine.paper.entry.entries.GroupEntry
import com.typewritermc.engine.paper.entry.entries.PersistableFactEntry

@Entry("permanent_fact", "Saved permanently, it never gets removed", Colors.PURPLE, "fa6-solid:database")
/**
 * This [fact](/docs/creating-stories/facts) is permanent and never expires.
 *
 * ## How could this be used?
 *
 * This could be used to store if a player joined the server before. If the player has completed a quest. Maybe if the player has received a reward already to prevent them from receiving it again.
 */
class PermanentFactEntry(
    override val id: String = "",
    override val name: String = "",
    override val comment: String = "",
    override val group: Ref<GroupEntry> = emptyRef(),
) : PersistableFactEntry
