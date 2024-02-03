package me.gabber235.typewriter.capture

import com.github.shynixn.mccoroutine.bukkit.ticks
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import lirand.api.extensions.events.SimpleListener
import lirand.api.extensions.events.listen
import lirand.api.extensions.events.unregister
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.ThreadType.SYNC
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent

class StaticRecorder<T>(private val player: Player, private val capturer: RecordedCapturer<T>) : Recorder<T> {
    private enum class RecordingState {
        WAITING_FOR_START,
        RECORDING,
        FINISHED,
    }

    private data class RecordingData<T>(
        val completer: CompletableDeferred<T>,
        val bossBar: BossBar,
        val listener: Listener,
    )


    private var data: RecordingData<T>? = null
    private var state = RecordingState.WAITING_FOR_START
    private var job: Job? = null

    override suspend fun record(): T {
        if (data != null) {
            throw IllegalStateException("Already recording!")
        }

        val recordingData = prepareRecording()
        data = recordingData
        return recordingData.completer.await()
    }

    private fun prepareRecording(): RecordingData<T> {
        val completer = CompletableDeferred<T>()

        val bossBar = BossBar.bossBar(
            "<aqua><bold>Waiting ${capturer.title}:</bold></aqua> Press <red><bold><key:key.swapOffhand></bold></red> to start recording".asMini(),
            1f,
            BossBar.Color.BLUE,
            BossBar.Overlay.PROGRESS
        )
        player.showBossBar(bossBar)
        player.playSound(Sound.sound(Key.key("block.beacon.activate"), Sound.Source.MASTER, 1f, 1f))

        val listener = SimpleListener()
        plugin.listen<PlayerSwapHandItemsEvent>(listener, block = this::onSwapHandItems)


        return RecordingData(completer, bossBar, listener)
    }

    private fun onTick(frame: Int) {
        if (data == null) return
        if (state != RecordingState.RECORDING) return
        capturer.captureFrame(player, frame)
    }

    private fun onSwapHandItems(event: PlayerSwapHandItemsEvent) {
        if (event.player.uniqueId != player.uniqueId) return
        if (data == null) return

        when (state) {
            RecordingState.WAITING_FOR_START -> {
                startRecording()
                event.isCancelled = true
            }

            RecordingState.RECORDING -> {
                stopRecording()
                event.isCancelled = true
            }

            RecordingState.FINISHED -> {}
        }
    }

    private fun startRecording() {
        if (state != RecordingState.WAITING_FOR_START) {
            throw IllegalStateException("Can only start recording when waiting for start!")
        }
        data?.bossBar?.let {
            it.name("<red><bold>Recording ${capturer.title}:</bold></red> Press <green><bold><key:key.swapOffhand></bold></green> to stop.".asMini())
            it.color(BossBar.Color.RED)
        }
        player.playSound(Sound.sound(Key.key("ui.button.click"), Sound.Source.MASTER, 1f, 1f))
        capturer.startRecording(player)

        job = SYNC.launch {
            var frame = 0
            while (state != RecordingState.FINISHED) {
                onTick(frame++)
                delay(1.ticks)
            }
        }

        state = RecordingState.RECORDING
    }

    private fun stopRecording() {
        if (state != RecordingState.RECORDING) {
            throw IllegalStateException("Can only stop recording when recording!")
        }
        data?.bossBar?.let { player.hideBossBar(it) }
        player.playSound(Sound.sound(Key.key("ui.cartography_table.take_result"), Sound.Source.MASTER, 1f, 1f))
        val result = capturer.stopRecording(player)
        data?.completer?.complete(result)
        data?.listener?.unregister()
        data = null
        job?.cancel()
        job = null
        state = RecordingState.FINISHED
    }
}