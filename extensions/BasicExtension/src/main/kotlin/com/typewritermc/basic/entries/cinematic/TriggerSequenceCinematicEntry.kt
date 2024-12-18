package com.typewritermc.basic.entries.cinematic

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.core.entries.emptyRef
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.InnerMax
import com.typewritermc.core.extension.annotations.Max
import com.typewritermc.core.extension.annotations.Segments
import com.typewritermc.core.interaction.context
import com.typewritermc.engine.paper.entry.*
import com.typewritermc.engine.paper.entry.entries.CinematicAction
import com.typewritermc.engine.paper.entry.entries.CinematicEntry
import com.typewritermc.engine.paper.entry.entries.EntryTrigger
import com.typewritermc.engine.paper.entry.entries.Segment
import com.typewritermc.engine.paper.entry.temporal.SimpleCinematicAction
import com.typewritermc.engine.paper.interaction.interactionContext
import org.bukkit.entity.Player

@Entry("trigger_sequence_cinematic", "A sequence of triggers to run", Colors.PURPLE, "fa-solid:play")
/**
 * The `Trigger Sequence Cinematic` entry that runs a sequence of triggers. It is very powerful but also very dangerous.
 *
 * :::caution
 * Be aware of which triggers are running. If you run a trigger that is viewable by everyone, it will be visible to everyone.
 * Also, **never trigger dialogues**, they should not be triggered from a cinematic.
 * If you want to trigger a dialogue after the cinematic, connect it to the [CinematicEntry](../action/cinematic.mdx)
 * :::
 *
 * ## How could this be used?
 * When you want to use any triggerable entry. Like giving an item to the player at a specific frame.
 */
class TriggerSequenceCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments
    @InnerMax(Max(1))
    val segments: List<TriggerSequenceSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return TriggerSequenceAction(
            player,
            this
        )
    }
}

data class TriggerSequenceSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
    val trigger: Ref<TriggerableEntry> = emptyRef()
) : Segment

class TriggerSequenceAction(
    val player: Player,
    entry: TriggerSequenceCinematicEntry
) : SimpleCinematicAction<TriggerSequenceSegment>() {
    override val segments: List<TriggerSequenceSegment> = entry.segments

    override suspend fun startSegment(segment: TriggerSequenceSegment) {
        super.startSegment(segment)

        val entry = segment.trigger.get() ?: return
        val context = player.interactionContext ?: context()
        EntryTrigger(entry).triggerFor(player, context)
    }
}