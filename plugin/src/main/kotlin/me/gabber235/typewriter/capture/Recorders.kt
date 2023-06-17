package me.gabber235.typewriter.capture

import com.github.shynixn.mccoroutine.bukkit.launch
import me.gabber235.typewriter.plugin
import me.gabber235.typewriter.utils.failure
import me.gabber235.typewriter.utils.ok
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.full.companionObject

interface Recorder<T> {
    suspend fun record(): T
}

class Recorders {
    private val recorders = ConcurrentHashMap<UUID, Recorder<*>>()

    fun isRecording(player: Player): Boolean {
        return recorders.containsKey(player.uniqueId)
    }

    suspend fun <T> record(
        player: Player,
        capturer: Capturer<T>,
        cinematic: String? = null,
        cinematicRange: IntRange?
    ): T {
        when (capturer) {
            is ImmediateCapturer -> return capturer.capture(player)
            is RecordedCapturer -> {
                if (cinematic == null || cinematicRange == null) {
                    return record(player, StaticRecorder(player, capturer))
                }

                return record(player, CinematicRecorder(player, capturer, cinematicRange, cinematic))
            }
        }
    }

    suspend fun <T> record(player: Player, recorder: Recorder<T>): T {
        if (recorders.containsKey(player.uniqueId)) {
            throw IllegalStateException("Already recording!")
        }
        recorders[player.uniqueId] = recorder
        val result = recorder.record()
        recorders.remove(player.uniqueId)
        return result
    }


    fun requestRecording(player: Player, context: RecorderRequestContext): RecorderResponse {
        if (isRecording(player)) {
            return RecorderResponse.AlreadyRecording
        }

        val recorderClass = try {
            Class.forName(context.recorderClassPath).kotlin
        } catch (e: ClassNotFoundException) {
            return RecorderResponse.RecorderNotFound
        }

        val capturerCreator = recorderClass.companionObject?.objectInstance

        if (capturerCreator !is CapturerCreator<*>) {
            return RecorderResponse.CapturerCreatorNotFound(context.recorderClassPath)
        }

        val capturerResult = capturerCreator.create(context)

        if (capturerResult.isFailure) {
            return RecorderResponse.RecorderCouldNotStart(capturerResult.exceptionOrNull()?.message ?: "Unknown reason")
        }

        val capturer = capturerResult.getOrThrow()

        plugin.launch {
            record(player, capturer, context.cinematic, context.cinematicRange)
        }

        return RecorderResponse.RecordingStarting
    }
}

interface CapturerCreator<C : Capturer<*>> {
    fun create(context: RecorderRequestContext): Result<C>
}

data class RecorderRequestContext(
    val recorderClassPath: String,
    val fieldPath: String,
    val fieldValue: Any?,
    val cinematic: String?,
    val cinematicRange: IntRange?,
) {
    val title: String
        get() {
            val field = fieldPath.split(".").last()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

            if (cinematic != null) {
                return "Recording $field in cinematic $cinematic"
            }

            return "Recording $field"
        }
}

sealed interface RecorderResponse {
    val message: String
    val status: RecorderResponseStatus

    fun toResult(): Result<String> {
        return if (status == RecorderResponseStatus.SUCCESS) {
            ok(message)
        } else {
            failure(message)
        }
    }

    object RecordingStarting : RecorderResponse {
        override val message: String = "Join the server to start recording!"
        override val status: RecorderResponseStatus = RecorderResponseStatus.SUCCESS
    }

    object RecorderNotFound : RecorderResponse {
        override val message: String =
            "The recorder for this field was not found! Report to the typewriter adapter developer."
        override val status: RecorderResponseStatus = RecorderResponseStatus.ERROR
    }

    class CapturerCreatorNotFound(recorderClassPath: String) : RecorderResponse {
        override val message: String =
            "The capturer creator for the recorder $recorderClassPath was not found! Report to the typewriter adapter developer."
        override val status: RecorderResponseStatus = RecorderResponseStatus.ERROR
    }

    object AlreadyRecording : RecorderResponse {
        override val message: String = "You are already recording!"
        override val status: RecorderResponseStatus = RecorderResponseStatus.ERROR
    }

    class RecorderCouldNotStart(reason: String) : RecorderResponse {
        override val message: String = "The recorder could not start: $reason"
        override val status: RecorderResponseStatus = RecorderResponseStatus.ERROR
    }
}

enum class RecorderResponseStatus {
    SUCCESS,
    ERROR
}