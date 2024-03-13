package me.gabber235.typewriter.entry.cinematic

import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.entry.entries.activeSegmentAt
import me.gabber235.typewriter.entry.entries.canFinishAt

abstract class SimpleCinematicAction<S : Segment> : CinematicAction {
    protected var lastFrame = 0
        private set
    private var previousSegment: S? = null

    override suspend fun tick(frame: Int) {
        lastFrame = frame
        super.tick(frame)
        val segment = segments activeSegmentAt frame

        if (segment == previousSegment) {
            segment?.let { tickSegment(it, frame) }
            return
        }

        previousSegment?.let { stopSegment(it) }

        segment?.let {
            startSegment(it)
            tickSegment(it, frame)
        }
    }

    override suspend fun teardown() {
        super.teardown()
        previousSegment?.let { stopSegment(it) }
        previousSegment = null
    }

    override fun canFinish(frame: Int): Boolean = segments canFinishAt frame

    protected open suspend fun startSegment(segment: S) {
        previousSegment = segment
    }

    protected open suspend fun stopSegment(segment: S) {
        previousSegment = null
    }

    protected open suspend fun tickSegment(segment: S, frame: Int) {
    }

    abstract val segments: List<S>
}