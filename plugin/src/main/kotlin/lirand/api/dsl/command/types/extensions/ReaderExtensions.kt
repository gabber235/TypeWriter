package lirand.api.dsl.command.types.extensions

import com.mojang.brigadier.StringReader

/**
 * Substrings the input between the current cursor of the [StringReader]
 * and the first encountered delimiter. It ignores quotation marks, i.e. given
 * `"hello world"`, the returned string will be `"hello`.
 *
 *
 * **This method should be preferred over [StringReader.readUnquotedString]
 * as the latter does not handle non-ASCII characters.**
 *
 * @return a string between the current cursor and the index of the first encountered
 * whitespace
 */
fun StringReader.readUnquoted(): String {
	return until(' ')
}

/**
 * Substrings the input between the current cursor of the [StringReader]
 * and index of the first encountered delimiter.
 *
 * @return a string between the current cursor and index of the first encountered
 * delimiter
 */
fun StringReader.until(delimiter: Char): String {
	val start = cursor
	while (canRead() && peek() != delimiter) {
		skip()
	}
	return string.substring(start, cursor)
}

/**
 * Substrings the input between the current cursor of the [StringReader]
 * and index of the delimiters first encountered.
 *
 * @return a string between the current cursor and the index of the first encountered
 * delimiter
 */
fun StringReader.until(vararg delimiters: Char): String {
	val start = cursor
	while (canRead() && peek() !in delimiters) {
		skip()
	}
	return string.substring(start, cursor)
}

/**
 * Substrings the input between the current cursor of the [StringReader]
 * and the index of the character for which the given predicate is `true`.
 *
 * @param end the predicate
 * @return a string between the current cursor and the index of the character
 * for which the predicate is `true`
 */
inline fun StringReader.until(crossinline end: (Char) -> Boolean): String {
	val start = cursor
	while (canRead() && !end(peek())) {
		skip()
	}
	return string.substring(start, cursor)
}