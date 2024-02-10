package me.gabber235.typewriter.entries.action

import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.Ref
import me.gabber235.typewriter.entry.TriggerableEntry
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.SoundId
import net.kyori.adventure.sound.SoundStop
import org.bukkit.entity.Player
import java.util.*

@Entry("stop_sound", "Stop a or all sounds for a player", Colors.RED, "teenyicons:sound-off-solid")
/**
 * The `Stop Sound` action is used to stop a or all sounds for a player.
 *
 * ## How could this be used?
 *
 * This action can be useful in situations where you want to stop a sound for a player.
 * For example, when leaving a certain area, you might want to stop the music that was playing.
 */
class StopSoundActionEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<Ref<TriggerableEntry>> = emptyList(),
    @Help("The sound to stop.")
    // The sound to stop. If this field is left blank, all sounds will be stopped.
    val sound: Optional<SoundId> = Optional.empty(),
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        if (sound.isPresent) {
            val sound = sound.get()
            val soundStop = sound.namespacedKey?.let { SoundStop.named(it) } ?: return

            player.stopSound(soundStop)
        } else {
            player.stopAllSounds()
        }
    }
}