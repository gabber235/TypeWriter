package com.typewritermc.core.utils.point

data class World(
    val identifier: String,
) {
    companion object {
        val Empty = World("")
    }
}