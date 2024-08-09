package lirand.api.utilities

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun Boolean.ifTrue(block: () -> Unit): Boolean {
	contract {
		callsInPlace(block, InvocationKind.AT_MOST_ONCE)
	}

	if (this) block()
	return this
}

@OptIn(ExperimentalContracts::class)
inline fun Boolean.ifFalse(block: () -> Unit): Boolean {
	contract {
		callsInPlace(block, InvocationKind.AT_MOST_ONCE)
	}

	if (!this) block()
	return this
}

fun <T> T.applyIfNotNull(block: (T.() -> Unit)?): T {
	if (block != null)
		apply(block)

	return this
}