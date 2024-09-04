package com.typewritermc.core.utils

import kotlin.math.max
import kotlin.math.min

fun loopingDistance(x: Int, y: Int, n: Int): Int {
    val max = max(x, y)
    val min = min(x, y)
    val first = max - min
    val second = n - (max - min - 1)
    return if (x < y) {
        if (first < second) first else -second
    } else {
        if (first < second) -first else second
    }
}

fun <T> List<T>.around(index: Int, before: Int = 1, after: Int = 1): List<T> {
    val total = before + after + 1
    return if (index <= before) subList(0, min(size, total))
    else if (size - index <= after) subList(max(0, size - total), size)
    else subList(index - before, index + after + 1)
}
