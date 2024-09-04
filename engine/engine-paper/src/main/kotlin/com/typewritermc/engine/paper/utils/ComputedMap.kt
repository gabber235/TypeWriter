package com.typewritermc.engine.paper.utils

class ComputedMap<K, V>(
    private val map: MutableMap<K, V> = mutableMapOf(),
    private val compute: (K) -> V?
) : Map<K, V> by map {
    override fun get(key: K): V? {
        return map[key] ?: compute(key)?.also { map[key] = it }
    }

    override fun getOrDefault(key: K, defaultValue: V): V {
        return get(key) ?: defaultValue
    }
}