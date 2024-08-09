package me.gabber235.typewriter.content.modes

/**
 * Tape is a map of ticks to values.
 * It is used to store the values of a recorder over time.
 *
 * The idea is that the recorder will store the values of the capturer in a tape every tick.
 * But only when the value changes.
 *
 * @param T The type of the values in the tape
 */
typealias Tape<T> = Map<Int, T>

/**
 * MutableTape is a mutable map of ticks to values.
 * It is used to store the values of a recorder over time.
 *
 * The idea is that the recorder will store the values of the capturer in a tape every tick.
 * But only when the value changes.
 *
 * @param T The type of the values in the tape
 */
typealias MutableTape<T> = MutableMap<Int, T>

fun <T> mutableTapeOf(): MutableTape<T> = mutableMapOf()

val <T> Tape<T>.minFrame: Int
    get() = keys.minOrNull() ?: 0

val <T> Tape<T>.maxFrame: Int
    get() = keys.maxOrNull() ?: 0

val <T> Tape<T>.duration: Int
    get() = maxFrame - minFrame

val <T> Tape<T>.firstFrame: T?
    get() = this[minFrame]

fun <T, E> Tape<T>.firstNotNullWhere(predicate: (T) -> E?): E? {
    return this.asSequence()
        .map { it.value }
        .firstNotNullOfOrNull(predicate)
}

/**
 * Get the value of the tape at the given frame.
 * If the frame is not present in the tape for the given frame,
 * The first frame before the given frame will be returned.
 *
 * If the frame is before the first frame, null will be returned.
 *
 * @param frame The frame to get the value of
 */
fun <T> Tape<T>.getFrame(frame: Int): T? {
    return this[frame] ?: this.asSequence()
        .filter { it.key <= frame }
        .maxByOrNull { it.key }
        ?.value
}

/**
 * Get the value inside the tape at the given frame.
 * If the frame is not present in the tape for the given frame,
 * The first frame before the given frame will be returned.
 *
 * If the frame is before the first frame, null will be returned.
 *
 * @param frame The frame to get the value of
 * @param getter The property to get the value of
 */
fun <T, E> Tape<T>.getFrame(frame: Int, getter: T.() -> E): E? {
    return this[frame]?.getter() ?: this.asSequence()
        .filter { it.key <= frame && it.value.getter() != null }
        .maxByOrNull { it.key }
        ?.value
        ?.getter()
}

/**
 * Get the value of the tape at the given frame.
 * If the frame is not present in the tape for the given frame,
 * null will be returned.
 *
 * @param frame The frame to get the value of
 */
fun <T, E> Tape<T>.getExactFrame(frame: Int, getter: T.() -> E): E? {
    return this[frame]?.getter()
}