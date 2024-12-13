package com.typewritermc.core.interaction

interface SessionTracker {
    fun setup()
    fun tick()
    fun teardown()
}