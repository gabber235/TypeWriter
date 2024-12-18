package com.typewritermc.example.entries.cinematic

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.*
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.entries.*
import com.typewritermc.engine.paper.entry.temporal.SimpleCinematicAction
import org.bukkit.entity.Player

//<code-block:cinematic_entry>
@Entry("example_cinematic", "An example cinematic entry", Colors.BLUE, "material-symbols:cinematic-blur")
class ExampleCinematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    @Segments(Colors.BLUE, "material-symbols:cinematic-blur")
    val segments: List<ExampleSegment> = emptyList(),
) : CinematicEntry {
    override fun create(player: Player): CinematicAction {
        return ExampleCinematicAction(player, this)
    }
}
//</code-block:cinematic_entry>

@Entry(
    "example_with_segment_sizes",
    "An example cinematic entry with segment sizes",
    Colors.BLUE,
    "material-symbols:cinematic-blur"
)
class ExampleWithSegmentSizesEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    //<code-block:cinematic_segment_with_min_max>
    @Segments(Colors.BLUE, "material-symbols:cinematic-blur")
    @InnerMin(Min(10))
    @InnerMax(Max(20))
    val segments: List<ExampleSegment> = emptyList(),
    //</code-block:cinematic_segment_with_min_max>
) : CinematicEntry {
    //<code-block:cinematic_create_actions>
    // This will be used when the cinematic is normally displayed to the player.
    override fun create(player: Player): CinematicAction {
        return DefaultCinematicAction(player, this)
    }

    // This is used during content mode to display the cinematic to the player.
    // It may be null to not show it during simulation.
    override fun createSimulating(player: Player): CinematicAction? {
        return SimulatedCinematicAction(player, this)
    }

    // This is used during content mode to record the cinematic.
    // It may be null to not record it during simulation.
    override fun createRecording(player: Player): CinematicAction? {
        return RecordingCinematicAction(player, this)
    }
    //</code-block:cinematic_create_actions>
}

//<code-block:cinematic_segment>
data class ExampleSegment(
    override val startFrame: Int = 0,
    override val endFrame: Int = 0,
) : Segment
//</code-block:cinematic_segment>

//<code-block:cinematic_action>
class ExampleCinematicAction(
    val player: Player,
    val entry: ExampleCinematicEntry,
) : CinematicAction {
    override suspend fun setup() {
        // Initialize variables, spawn entities, etc.
    }

    override suspend fun tick(frame: Int) {
        val segment = entry.segments activeSegmentAt frame
        // Can be null if no segment is active

        // The `frame` parameter is not necessarily next frame: `frame != old(frame)+1`

        // Execute tick logic for the segment
    }

    override suspend fun teardown() {
        // Remove entities, etc.
    }

    override fun canFinish(frame: Int): Boolean = entry.segments canFinishAt frame
}
//</code-block:cinematic_action>

//<code-block:cinematic_simple_action>
class ExampleSimpleTemporalAction(
    val player: Player,
    entry: ExampleCinematicEntry,
) : SimpleCinematicAction<ExampleSegment>() {
    override val segments: List<ExampleSegment> = entry.segments

    override suspend fun startSegment(segment: ExampleSegment) {
        super.startSegment(segment) // Keep this
        // Called when a segment starts
    }

    override suspend fun tickSegment(segment: ExampleSegment, frame: Int) {
        super.tickSegment(segment, frame) // Keep this
        // Called every tick while the segment is active
        // Will always be called after startSegment and never after stopSegment

        // The `frame` parameter is not necessarily next frame: `frame != old(frame)+1`
    }

    override suspend fun stopSegment(segment: ExampleSegment) {
        super.stopSegment(segment) // Keep this
        // Called when the segment ends
        // Will also be called if the cinematic is stopped early
    }
}
//</code-block:cinematic_simple_action>

class DefaultCinematicAction(
    val player: Player,
    entry: ExampleWithSegmentSizesEntry,
) : SimpleCinematicAction<ExampleSegment>() {
    override val segments: List<ExampleSegment> = entry.segments
}

class SimulatedCinematicAction(
    val player: Player,
    entry: ExampleWithSegmentSizesEntry,
) : SimpleCinematicAction<ExampleSegment>() {
    override val segments: List<ExampleSegment> = entry.segments
}

class RecordingCinematicAction(
    val player: Player,
    entry: ExampleWithSegmentSizesEntry,
) : SimpleCinematicAction<ExampleSegment>() {
    override val segments: List<ExampleSegment> = entry.segments
}