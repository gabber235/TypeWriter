package com.typewritermc.services.libs.utils

import kotlin.time.Duration
import kotlinx.coroutines.delay as coroutineDelay

/** Abstracts waiting so lifecycle code can use real or deterministic scheduling. */
fun interface DelayScheduler {
    /** Suspends for [duration] and propagates coroutine cancellation. */
    suspend fun delay(duration: Duration)
}

/** Production scheduler backed by kotlinx.coroutines delay. */
object CoroutineDelayScheduler : DelayScheduler {
    override suspend fun delay(duration: Duration) {
        coroutineDelay(duration)
    }
}
