package me.gabber235.typewriter.utils

import com.github.shynixn.mccoroutine.launchAsync
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.gabber235.typewriter.Typewriter.Companion.plugin
import kotlin.time.Duration

class Cooldown(private val duration: Duration, private val invoker: () -> Unit) {
	private var job: Job? = null
	operator fun invoke() {
		if (job == null) {
			job = plugin.launchAsync {
				delay(duration)
				job = null
				invoker()
			}
		}
	}
}