package me.gabber235.typewriter.utils

import me.gabber235.typewriter.entry.Query
import me.gabber235.typewriter.entry.entries.SoundIdEntry
import me.gabber235.typewriter.logger
import org.bukkit.NamespacedKey

sealed interface SoundId {
    companion object {
        val EMPTY = DefaultSoundId(null)
    }

    val namespacedKey: NamespacedKey?
}

class DefaultSoundId(override val namespacedKey: NamespacedKey?) : SoundId

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