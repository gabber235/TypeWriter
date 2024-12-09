package com.typewritermc.engine.paper.interaction

import java.time.Duration

interface Interaction {
    val priority: Int
    suspend fun initialize(): Result<Unit>
    suspend fun tick(deltaTime: Duration)
    suspend fun teardown(force: Boolean = false)
}