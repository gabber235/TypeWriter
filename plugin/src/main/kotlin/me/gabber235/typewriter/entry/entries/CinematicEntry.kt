package me.gabber235.typewriter.entry.entries

import me.gabber235.typewriter.adapters.Tags
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.*
import org.bukkit.entity.Player

@Tags("cinematic")
interface CinematicEntry<S : Segment> : Entry {
	@Help("The criteria that must be met before this entry is shown")
	val criteria: List<Criteria>

	@Help("The segments that define the action")
	val segments: List<S>

	fun create(player: Player): CinematicAction
}

infix fun <S : Segment> CinematicEntry<S>.activeSegmentsAt(frame: Int) = segments.filter { it isActiveAt frame }

interface Segment {
	val startFrame: Int
	val endFrame: Int
}

infix fun Segment.isActiveAt(frame: Int): Boolean = frame in startFrame..endFrame
infix fun Segment.canFinishAt(frame: Int): Boolean = frame > endFrame

interface CinematicAction {
	fun setup() {}
	fun tick(frame: Int) {}
	fun teardown() {}

	infix fun canFinish(frame: Int): Boolean
}

val CinematicEntry<*>.duration: Int
	get() = segments.maxOf { it.endFrame }

infix fun CinematicEntry<*>.canFinishAt(frame: Int): Boolean {
	return frame > duration
}
