package me.gabber235.typewriter.entries.cinematic

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.adapters.modifiers.InnerMax
import me.gabber235.typewriter.adapters.modifiers.Max
import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.cinematic.SimpleCinematicAction
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.CinematicEntry
import me.gabber235.typewriter.entry.entries.EntryTrigger
import me.gabber235.typewriter.entry.entries.Segment
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
    @Help("The sequence of triggers to run")
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
        EntryTrigger(entry) triggerFor player
    }
}