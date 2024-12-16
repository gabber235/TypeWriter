package com.typewritermc.core.utils

fun String.replaceAll(vararg pairs: Pair<String, String>): String {
    return pairs.fold(this) { acc, (from, to) ->
        acc.replace(from, to)
    }
}
