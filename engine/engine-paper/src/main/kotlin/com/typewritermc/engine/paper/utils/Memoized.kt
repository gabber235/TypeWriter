package com.typewritermc.engine.paper.utils

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Memoized<in T, out R>(val block: (T) -> R) {
	private val cache = mutableMapOf<T, R>()

	operator fun invoke(arg: T): R {
		return cache.getOrPut(arg) { block(arg) }
	}
}

class MemoizedDelegate<in T, out R>(private val block: (T) -> R) : ReadOnlyProperty<Any?, Memoized<T, R>> {
	private var memoized: Memoized<T, R>? = null

	override fun getValue(thisRef: Any?, property: KProperty<*>): Memoized<T, R> {
		return memoized ?: Memoized(block).also { memoized = it }
	}
}

fun <T, R> memoized(block: (T) -> R) = MemoizedDelegate(block)