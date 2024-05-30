package me.gabber235.typewriter.utils

import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.SoundIdEntry
import me.gabber235.typewriter.entry.entries.SoundSourceEntry
import me.gabber235.typewriter.logger
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.sound.SoundStop
import org.bukkit.Location
import org.bukkit.NamespacedKey
import net.kyori.adventure.sound.Sound as AdventureSound


data class Sound(
    @Help("The sound to play.")
    val soundId: SoundId = SoundId.EMPTY,
    @Help("The source of the location to play the sound from. (Defaults to player's location)")
    val soundSource: SoundSource = SelfSoundSource,
    @Help("The track to play the sound on. (Corresponds to the Minecraft sound category)")
    val track: AdventureSound.Source = AdventureSound.Source.MASTER,
    @Help("The volume of the sound. A value of 1.0 is normal volume.")
    val volume: Float = 1.0f,
    @Help("The pitch of the sound. A value of 1.0 is normal pitch.")
    val pitch: Float = 1.0f,
) {

    companion object {
        val EMPTY = Sound()
    }

    val soundStop: SoundStop?
        get() = soundId.namespacedKey?.let { SoundStop.named(it) }

    fun play(audience: Audience) {
        val key = this.soundId.namespacedKey ?: return
        val sound = AdventureSound.sound(key, track, volume, pitch)

        when (soundSource) {
            is SelfSoundSource -> audience.playSound(sound)
            is EmitterSoundSource -> {
                val entryId = soundSource.entryId
                val entry = Query.findById<SoundSourceEntry>(entryId)
                if (entry == null) {
                    logger.warning("Could not find sound source entry with id $entryId")
                    return
                }
                val emitter = entry.getEmitter()
                audience.playSound(sound, emitter)
            }

            is LocationSoundSource -> {
                val location = soundSource.location
                audience.playSound(sound, location.x, location.y, location.z)
            }
        }
    }
}

fun Audience.playSound(sound: Sound) = sound.play(this)
fun Audience.stopSound(sound: Sound) = sound.soundStop?.let { this.stopSound(it) }

sealed interface SoundId {
    companion object {
        val EMPTY = DefaultSoundId(null)
    }

    val namespacedKey: NamespacedKey?
}

class DefaultSoundId(override val namespacedKey: NamespacedKey?) : SoundId {
    constructor(key: String) : this(if (key.isEmpty()) null else NamespacedKey.fromString(key))
}

class EntrySoundId(val entryId: String) : SoundId {
    override val namespacedKey: NamespacedKey?
        get() {
            val entry = Query.findById<SoundIdEntry>(entryId)
            if (entry == null) {
                logger.warning("Could not find sound entry with id $entryId")
                return null
            }
            return NamespacedKey.fromString(entry.soundId)
        }
}

sealed interface SoundSource

data object SelfSoundSource : SoundSource
class EmitterSoundSource(val entryId: String) : SoundSource

class LocationSoundSource(val location: Location) : SoundSource
