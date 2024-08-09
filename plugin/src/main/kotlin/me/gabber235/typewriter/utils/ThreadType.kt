package me.gabber235.typewriter.utils

import com.github.shynixn.mccoroutine.bukkit.asyncDispatcher
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import lirand.api.extensions.server.server
import me.gabber235.typewriter.plugin

enum class ThreadType {
    SYNC,
    ASYNC,
    DISPATCHERS_ASYNC,
    REMAIN,
    ;

    suspend fun <T> switchContext(block: suspend () -> T): T {
        if (!plugin.isEnabled) {
            return block()
        }
        if (this == REMAIN) {
            return block()
        }

        return withContext(
            when (this) {
                SYNC -> plugin.minecraftDispatcher
                ASYNC -> plugin.asyncDispatcher
                DISPATCHERS_ASYNC -> Dispatchers.IO
                else -> throw IllegalStateException("Unknown thread type: $this")
            }
        ) {
            block()
        }
    }

    fun launch(block: suspend () -> Unit): Job {
        if (!plugin.isEnabled) {
            runBlocking {
                block()
            }
            return Job()
        }

        return plugin.launch(
            when (this) {
                SYNC -> plugin.minecraftDispatcher
                ASYNC -> plugin.minecraftDispatcher
                DISPATCHERS_ASYNC -> Dispatchers.IO
                REMAIN -> if (server.isPrimaryThread) plugin.minecraftDispatcher else plugin.asyncDispatcher
            }
        ) {
            block()
        }
    }
}