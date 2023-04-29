package me.gabber235.typewriter.entry.cinematic

import me.gabber235.typewriter.adapters.modifiers.Segments
import me.gabber235.typewriter.entry.entries.CinematicAction
import me.gabber235.typewriter.entry.entries.Segment
import me.gabber235.typewriter.entry.entries.activeSegmentAt
import me.gabber235.typewriter.entry.entries.canFinishAt

abstract class SimpleCinematicAction<S: Segment> : CinematicAction {
	private var previousSegment: S? = null

	override fun tick(frame: Int) {
		super.tick(frame)
		val segment = segments activeSegmentAt frame

		if(segment == previousSegment)
			return

		previousSegment?.let { stopSegment(it) }

		segment?.let { startSegment(it) }
	}

	override fun canFinish(frame: Int): Boolean = segments canFinishAt frame

	protected open fun startSegment(segment: S) {
		previousSegment = segment
	}

	protected open fun stopSegment(segment: S) {
		previousSegment = null
	}

	abstract val segments: List<S>
}