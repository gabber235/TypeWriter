package com.typewritermc.engine.paper.interaction

interface SessionTracker {
    fun setup()
    fun tick()
    fun teardown()
}