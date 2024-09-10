package com.typewritermc.basic.entries.action

import com.typewritermc.core.books.pages.Colors
import com.typewritermc.core.extension.annotations.Entry
import com.typewritermc.core.extension.annotations.Help
import com.typewritermc.engine.paper.entry.Criteria
import com.typewritermc.engine.paper.entry.Modifier
import com.typewritermc.core.entries.Ref
import com.typewritermc.engine.paper.entry.TriggerableEntry
import com.typewritermc.engine.paper.entry.entries.ActionEntry
import com.typewritermc.engine.paper.utils.SoundId
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
    @Help("The sound to stop. If this field is left blank, all sounds will be stopped.")
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