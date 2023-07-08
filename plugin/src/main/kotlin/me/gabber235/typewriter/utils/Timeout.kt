package me.gabber235.typewriter.utils

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.gabber235.typewriter.plugin
import org.koin.core.component.KoinComponent
import kotlin.time.Duration

class Timeout(
    private val duration: Duration,
    private val invoker: () -> Unit,
    private val immediateRunnable: Runnable? = null,
) : KoinComponent {
    private var job: Job? = null
    operator fun invoke() {
        immediateRunnable?.run()
        if (job == null) {
            job = plugin.launch(Dispatchers.IO) {
                delay(duration)
                job = null
                invoker()
            }
        }
    }

    fun cancel() {
        job?.cancel()
        job = null
    }
}