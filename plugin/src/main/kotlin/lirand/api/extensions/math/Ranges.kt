package lirand.api.extensions.math

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.block.Block

operator fun PosRange<*, BlockPosition>.contains(other: Location) = contains(other.asPosition())
operator fun PosRange<*, BlockPosition>.contains(other: Block) = contains(other.asPosition())
operator fun PosRange<*, ChunkPosition>.contains(other: Chunk) = contains(other.asPosition())

operator fun Location.rangeTo(other: Location): PosRange<Location, BlockPosition> {
	return PosRange(this.asBlockPosition(), other.asBlockPosition()) {
		RangeIteratorWithFactor<Location, BlockPosition>(
			this, other,
			{ it.asLocation(world) },
			{ it.asBlockPosition() }
		)
	}
}

operator fun Block.rangeTo(other: Block): PosRange<Block, BlockPosition> {
	return PosRange(this.asPosition(), other.asPosition()) {
		RangeIteratorWithFactor<Block, BlockPosition>(
			this, other,
			{ it.asBlock(world) },
			{ it.asPosition() }
		)
	}
}

operator fun Chunk.rangeTo(other: Chunk): PosRange<Chunk, ChunkPosition> {
	return PosRange(this.asPosition(), other.asPosition()) {
		RangeIteratorWithFactor<Chunk, ChunkPosition>(
			this, other,
			{ it.asBukkitChunk(world) },
			{ it.asPosition() }
		)
	}
}

class PosRange<T, P : Position<P>>(
	val first: P,
	val last: P,
	val buildIterator: () -> Iterator<T>
) : ClosedRange<P>, Iterable<T> {
	override val endInclusive: P get() = last
	override val start: P get() = first

	override fun contains(value: P): Boolean {
		val firstAxis = first.axis()
		val lastAxis = last.axis()
		return value.axis().withIndex().all { (index, it) ->
			it >= firstAxis[index] && it <= lastAxis[index]
		}
	}

	override fun iterator(): Iterator<T> = buildIterator()
}

class PosRangeIterator<T : Position<T>>(
	first: T,
	last: T,
	val factor: (axis: IntArray) -> T
) : Iterator<T> {
	private val firstAxis = first.axis()
	private val lastAxis = last.axis()
	private val closedAxisRanges = firstAxis.mapIndexed { index, it ->
		IntProgression.fromClosedRange(it.toInt(), lastAxis[index].toInt(), 1)
	}
	private val iteratorAxis = closedAxisRanges.map { it.iterator() }.toTypedArray()

	private val actualAxis = iteratorAxis.toList().subList(0, iteratorAxis.size - 1)
		.map { it.nextInt() }
		.toTypedArray()

	override fun hasNext(): Boolean {
		return iteratorAxis.any { it.hasNext() }
	}

	override fun next(): T {
		val lastIndex = iteratorAxis.size - 1
		val last = iteratorAxis[lastIndex]
		if (last.hasNext()) {
			val axis = IntArray(actualAxis.size) { actualAxis[it] } + last.nextInt()
			return factor(axis)
		}
		for (i in lastIndex - 1 downTo 0) {
			val axis = iteratorAxis[i]
			if (axis.hasNext()) {
				actualAxis[i] = axis.nextInt()
				iteratorAxis[i + 1] = closedAxisRanges[i + 1].iterator()
				break
			}
		}
		return next()
	}
}

class RangeIteratorWithFactor<T, P : Position<P>>(
	start: T,
	end: T,
	private val factor: (P) -> T,
	private val posFactor: (T) -> P
) : Iterator<T> {
	val iterator = PosRangeIterator(posFactor(start), posFactor(end), posFactor(start)::factor)

	override fun hasNext() = iterator.hasNext()
	override fun next() = factor(iterator.next())
}
