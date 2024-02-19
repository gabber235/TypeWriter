package me.gabber235.typewriter.capture

import kotlinx.coroutines.CompletableDeferred
import lirand.api.extensions.events.SimpleListener
import lirand.api.extensions.events.listen
import lirand.api.extensions.events.unregister
import me.gabber235.typewriter.entry.dialogue.confirmationKey
import me.gabber235.typewriter.entry.entries.CinematicStartTrigger
import me.gabber235.typewriter.entry.triggerFor
import me.gabber235.typewriter.events.AsyncCinematicEndEvent
import me.gabber235.typewriter.events.AsyncCinematicTickEvent
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.asMini
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import java.util.*


class CinematicRecorder<T>(
    private val player: Player,
    private val capturer: RecordedCapturer<T>,
    private val cinematic: String,
    private val frames: IntRange,
    private val ignoredEntries: List<String>,
) : Recorder<T> {
    private enum class RecordingState {
        WAITING_FOR_START,
        PRE_SEGMENT,
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
            "Waiting <aqua>${capturer.title}</aqua>: Press <red><bold>${confirmationKey.keybind}</bold></red> to start recording".asMini(),
            1f,
            BossBar.Color.BLUE,
            BossBar.Overlay.PROGRESS
        )
        player.showBossBar(bossBar)
        player.playSound(Sound.sound(Key.key("block.beacon.activate"), Sound.Source.MASTER, 1f, 1f))

        val listener = SimpleListener()
        confirmationKey.listen(listener, player.uniqueId, block = this::onConfirmation)
        plugin.listen<AsyncCinematicTickEvent>(listener, block = this::onTick)
        plugin.listen<AsyncCinematicEndEvent>(listener, block = this::onCinematicEnd)

        return RecordingData(completer, bossBar, listener)
    }

    private fun onConfirmation() {
        if (data == null) return
        if (state != RecordingState.WAITING_FOR_START) return

        startCinematic()
    }

    private fun startCinematic() {
        state = RecordingState.PRE_SEGMENT
        player.playSound(Sound.sound(Key.key("block.beacon.activate"), Sound.Source.MASTER, 1f, 1f))

        CinematicStartTrigger(
            cinematic,
            override = true,
            simulate = true,
            ignoreEntries = ignoredEntries,
            minEndTime = Optional.of(frames.last + 1)
        ) triggerFor player
    }

    private fun onCinematicEnd(event: AsyncCinematicEndEvent) {
        // If the cinematic ends before the end of the recording, we need to stop the recording
        if (event.player.uniqueId != player.uniqueId) return
        if (data == null) return
        if (state == RecordingState.WAITING_FOR_START || state == RecordingState.FINISHED) return

        stopRecording()
    }

    private fun onTick(event: AsyncCinematicTickEvent) {
        if (event.player.uniqueId != player.uniqueId) return
        val frame = event.frame
        if (data == null) return
        if (frame < frames.first) {
            preStart(frame)
            return
        }
        if (frame == frames.first) {
            startRecording()
        }
        if (frame >= frames.last) {
            stopRecording()
            return
        }

        tickDuringRecording(frame)
    }


    private fun preStart(frame: Int) {
        val secondsLeft = (frames.first - frame) / 20

        val color = when {
            secondsLeft <= 1 -> "red"
            secondsLeft <= 3 -> "#de751f"
            secondsLeft <= 5 -> "yellow"
            else -> "green"
        }

        data?.bossBar?.let {
            it.name("<gold><bold>Viewing ${capturer.title}:</bold></gold> Starting recording in <$color><bold>$secondsLeft</bold></$color>".asMini())
            it.color(BossBar.Color.YELLOW)
            it.progress(1f - (frame / frames.first.toFloat()))
        }

        if (secondsLeft > 5) return
        if ((frames.first - frame) % 20 != 0) return
        player.playSound(
            Sound.sound(
                Key.key("block.note_block.bell"),
                Sound.Source.MASTER,
                1f,
                1f - (secondsLeft * 0.1f)
            )
        )
    }

    private fun tickDuringRecording(frame: Int) {
        val secondsLeft = (frames.last - frame) / 20


        data?.bossBar?.let {
            it.name("<gold><bold>Viewing ${capturer.title}:</bold></gold> Recording ends in <bold>$secondsLeft</bold>".asMini())
            it.color(BossBar.Color.YELLOW)
            it.progress((frame - frames.first) / (frames.last - frames.first).toFloat())
        }

        val correctedFrame = frame - frames.first
        capturer.captureFrame(player, correctedFrame)
    }

    private fun startRecording() {
        if (state != RecordingState.WAITING_FOR_START && state != RecordingState.PRE_SEGMENT) {
            throw IllegalStateException("Can only start recording when waiting for start!")
        }
        player.playSound(Sound.sound(Key.key("ui.button.click"), Sound.Source.MASTER, 1f, 1f))
        capturer.startRecording(player)
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
        state = RecordingState.FINISHED
    }
}