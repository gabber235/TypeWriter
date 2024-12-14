package com.typewritermc.core.interaction

interface InteractionBound {
    val priority: Int

    fun initialize() {}
    fun tick() {}
    fun teardown() {}

    object Empty : InteractionBound {
        override val priority: Int = -1
        override fun initialize() {}
        override fun tick() {}
        override fun teardown() {}
    }
}

enum class InteractionBoundState {
    BLOCKING,
    INTERRUPTING,
    IGNORING,
}